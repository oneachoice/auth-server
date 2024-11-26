package oneachoice.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import oneachoice.auth.exception.TokenException;
import oneachoice.auth.util.CookieUtil;
import oneachoice.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    private final RefreshTokenCahcingService refreshTokenCahcingService;

    @Value("${jwt.ttl.refresh}")
    private long refreshTTL;

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
        if(refreshToken == null || refreshToken.isBlank()) {
            throw new TokenException("요청에 토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        
        // 리프레쉬 토큰 만료 여부 확인
        if(jwtUtil.isExpired(refreshToken)) {
            throw new TokenException("토큰이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }
        
        // 리프레쉬 토큰에서 카테고리 추출
        String category = jwtUtil.getCategory(refreshToken);
        
        // 리프레쉬 토큰 카테코리 유효성 확인
        if(!category.equals("refresh")) {
            throw new TokenException("토큰의 카테고리가 refresh가 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        // 리프레쉬 토큰 캐시 존재 확인
        if(!refreshTokenCahcingService.existsByToken(refreshToken)) {
            throw new TokenException("찾을 수 없는 Refresh 토큰입니다.", HttpStatus.BAD_REQUEST);
        }

        // 이메일과 역할 추출
        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 엑세스 토큰과 리프레쉬 토큰 발급
        String newAccessToken = jwtUtil.createJwt("access", email, role);
        String newRefreshToken = jwtUtil.createJwt("refresh", email, role);

        // 기존 캐시에서 전에 발급한 refresh 토큰 제거
        refreshTokenCahcingService.removeByToken(refreshToken);
        // 새로 발급한 refresh 토큰 캐시에 추가
        refreshTokenCahcingService.add(email, role, newRefreshToken);

        // 엑세스토큰은 헤더에 설정
        response.setHeader("access", newAccessToken);
        // 리프레쉬 토큰은 쿠키에 저장
        response.addCookie(cookieUtil.createCookie("refresh", newRefreshToken, refreshTTL));
    }
}
