package walid.jahin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/buy", "/api/contact").permitAll()  // 비인증 허용
                .anyRequest().authenticated()             // 나머지는 인증 필요
            )
            .csrf(csrf -> csrf.disable())                // CSRF 비활성화 (POST 요청 허용)
            .httpBasic(Customizer.withDefaults())        // 기본 로그인 허용 (원하는 방식으로 바꿔도 됨)
            .build();
    }
}
