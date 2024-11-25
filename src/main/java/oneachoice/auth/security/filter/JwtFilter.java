package oneachoice.auth.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oneachoice.auth.database.mysql.entity.UserEntity;
import oneachoice.auth.dto.inside.UserDTO;
import oneachoice.auth.security.CustomUserDetails;
import oneachoice.auth.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 모든 요청은 딱 한번만 이 필터를 거침
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더 찾기
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.debug("토큰이 존재하지 않음");
            filterChain.doFilter(request, response);

            return;
        }

        String token = authorization.split(" ")[1];
        
        // 토큰 만료 확인
        if(jwtUtil.isExpired(token)) {
            log.debug("토큰이 만료됨");
            filterChain.doFilter(request, response);

            return;
        }


        // 토큰에서 email과 role 휙득
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        // UserDTO 생성
        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .role(role)
                .build();

        // UserDTO에 유저 정보 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

        
    }
}
