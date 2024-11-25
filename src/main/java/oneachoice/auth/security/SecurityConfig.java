package oneachoice.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                .authorizeHttpRequests(config -> config
                        .requestMatchers("/login", "/join").permitAll()
                        //테스트용 임시 경로
                        .requestMatchers("/hello").permitAll()
                        .anyRequest().authenticated()
                );
        
        http
                // 세션 상태는 STATELESS하게 유지, JWT 방식 사용
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );


        return http.build();
    }
}
