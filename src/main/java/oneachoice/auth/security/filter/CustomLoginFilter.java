package oneachoice.auth.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import oneachoice.auth.security.CustomUserDetails;
import oneachoice.auth.service.RefreshTokenCahcingService;
import oneachoice.auth.util.CookieUtil;
import oneachoice.auth.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Builder
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenCahcingService refreshTokenCahcingService;

    private final long refreshTTL;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 요청에서 email과 password 추출
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 검증을 위해 토큰에 담아야함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        // 토큰을 AuthenticationManager로 전달함
        return authenticationManager.authenticate(authToken);
    }

    // 인증 성공시 실행되는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        // 로그인에 성공한 유저 이메일 추출
        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();

        // 역할 추출
        String role = authority.getAuthority();

        // access 토큰 생성
        String accessToken = jwtUtil.createJwt("access", email, role);
        // refresh 토큰 생성
        String refreshToken = jwtUtil.createJwt("refresh", email, role);

        // refresh 토큰 캐싱
        refreshTokenCahcingService.add(email, role, refreshToken);

        // 응답 설정
        response.setHeader("access", accessToken);
        response.addCookie(cookieUtil.createCookie("refresh", refreshToken, refreshTTL));
        response.setStatus(HttpStatus.OK.value());
    }

    // 인증 실패시 실행되는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
