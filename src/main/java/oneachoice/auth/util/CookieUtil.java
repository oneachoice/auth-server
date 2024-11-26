package oneachoice.auth.util;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${jwt.ttl.refresh}")
    private long refreshTTL;

    // 쿠키 생성 메서드
    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((int) refreshTTL / 1000);
        // cookie.setSecure(true);
        cookie.setPath("/reissue");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
