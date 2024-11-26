package oneachoice.auth.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    // 쿠키 생성 메서드
    public Cookie createCookie(String key, String value, long refreshTTL) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((int) refreshTTL / 1000);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
