package oneachoice.auth.security;

import lombok.RequiredArgsConstructor;
import oneachoice.auth.security.filter.CustomLogoutFilter;
import oneachoice.auth.security.filter.JwtFilter;
import oneachoice.auth.security.filter.CustomLoginFilter;
import oneachoice.auth.service.RefreshTokenCahcingService;
import oneachoice.auth.util.CookieUtil;
import oneachoice.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final CustomCorsConfigurationSource customCorsConfigurationSource;

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    private final RefreshTokenCahcingService refreshTokenCahcingService;

    @Value("${jwt.ttl.refresh}")
    private long refreshTTL;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF 방지 기능 사용 안함, JWT 방식이라 필요없음
                .csrf(config -> config.disable());

        http
                // Form 로그인 사용 안함
                .formLogin(config -> config.disable());

        http
                // Http Basic 사용 안함
                .httpBasic(config -> config.disable());

        http
                // 경로 인가 매핑
                .authorizeHttpRequests(config -> config.requestMatchers("/login", "/join", "/reissue").permitAll()
                        //테스트용 임시 경로
                        .requestMatchers("/hello").hasRole("USER").anyRequest().authenticated());

        http
                // 세션 상태는 STATELESS하게 유지, JWT 방식 사용
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                // FormLogin에서 UsernamePasswordAuthenticationFilter가 Disabled됨
                // UsernamePasswordAuthenticationFilter를 상속한 LoginFilter를 그 자리에 넣어줌
                .addFilterAt(CustomLoginFilter.builder()
                        .jwtUtil(jwtUtil)
                        .cookieUtil(cookieUtil)
                        .authenticationManager(authenticationManager(authenticationConfiguration))
                        .refreshTokenCahcingService(refreshTokenCahcingService)
                        .refreshTTL(refreshTTL)
                        .build(), UsernamePasswordAuthenticationFilter.class);

        http
                // JWT 인증 필터 추가, 로그인 필터 전에 검증해서 인증해줌
                .addFilterBefore(new JwtFilter(jwtUtil), CustomLoginFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenCahcingService), LogoutFilter.class);

        http
                // CORS 정책 설정
                .cors(config -> config.configurationSource(customCorsConfigurationSource));


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
