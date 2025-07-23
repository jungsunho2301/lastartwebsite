package walid.jahin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                //.requestMatchers("/login.html", "/css/**", "/js/**", "/assets/**").permitAll()
                //.requestMatchers("/api/admin/login", "/admin/api/artworks/**", "/api/artworks", "/api/artshop").permitAll()
                .anyRequest().permitAll()   //.authenticated()
            )
            .csrf(csrf -> csrf.disable()) // HTML 폼 테스트용: CSRF 임시로 끔
            .formLogin(form -> form.disable()) // 우리가 직접 만든 login.html 쓰기 때문에 disable
            .httpBasic(httpBasic -> httpBasic.disable())  // ✅ 인증 팝업 안 뜨게 함
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            );

        return http.build();
    }
}
