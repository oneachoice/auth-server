package oneachoice.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import oneachoice.auth.exception.TokenException;
import oneachoice.auth.util.CookieUtil;
import oneachoice.auth.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    public void reissue(HttpServletRequest request, HttpServletResponse response) throws TokenException {

        // 리프레쉬 토큰 추출
        Cookie[] cookies = request.getCookies();

        String refreshToken = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        // 리프레쉬 토큰 존재 여부 학인
        if(refreshToken == null) {
            throw new TokenException("Refresh token doesn't exist", HttpStatus.BAD_REQUEST);
        }
        
        // 리프레쉬 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException ex) {
            throw new TokenException("Token is expired", HttpStatus.BAD_REQUEST, ex);
        }
        
        String category = jwtUtil.getCategory(refreshToken);
        
        // 리프레쉬 토큰 카테코리 유효성 확인
        if(!category.equals("refresh")) {
            throw new TokenException("Token has invalid category", HttpStatus.BAD_REQUEST);
        }

        // 이메일과 역할 추출
        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 엑세스 토큰과 리프레쉬 토큰 발급
        String newAccessToken = jwtUtil.createJwt("access", email, role);
        String newRefreshToken = jwtUtil.createJwt("refresh", email, role);

        // 엑세스토큰은 헤더에 설정
        response.setHeader("access", newAccessToken);
        // 리프레쉬 토큰은 쿠키에 저장
        response.addCookie(cookieUtil.createCookie("refresh", newRefreshToken));
    }
}
