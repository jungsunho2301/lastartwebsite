package walid.jahin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import jakarta.servlet.http.HttpServletResponse;
import walid.jahin.security.AdminDetailsService;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/admin/login", "/api/admin/login",
                                                                "/api/admin/check-session")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                .anyRequest().permitAll())
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                                .ignoringRequestMatchers(
                                                                "/api/buy",
                                                                "/api/inquiry",
                                                                "/api/subscribe",
                                                                "/api/admin/login",
                                                                "/api/admin/logout"))
                                .exceptionHandling(eh -> eh
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        System.out.println(
                                                                        "접근 거부: " + accessDeniedException.getMessage());
                                                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                                }))
                                .formLogin(form -> form.disable())
                                .httpBasic(httpBasic -> httpBasic.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                                .maximumSessions(1)
                                                .maxSessionsPreventsLogin(false)
                                                .expiredUrl("/admin/login?expired"))
                                .securityContext(context -> context.requireExplicitSave(false));

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
                return new HttpSessionEventPublisher();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, AdminDetailsService adminDetailsService)
                        throws Exception {
                AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
                builder.userDetailsService(adminDetailsService)
                                .passwordEncoder(passwordEncoder());
                return builder.build();
        }

}