package walid.jahin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").authenticated()  // admin 경로는 인증 필요
                .anyRequest().permitAll()
            )
            .csrf(withDefaults())
            .formLogin(form -> form.disable())  // 우리가 직접 만든 login.html 쓰기 때문에 disable
            .httpBasic(httpBasic -> httpBasic.disable())  // 인증 팝업 방지
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            );

        return http.build();
    }

    // ✅ BCrypt 비밀번호 인코더 Bean 등록 (로그인 검증에 필요)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
