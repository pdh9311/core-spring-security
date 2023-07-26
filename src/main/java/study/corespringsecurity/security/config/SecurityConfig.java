package study.corespringsecurity.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 *  [WebSecurityCustomizer 의 ignoring() 과 SecurityFilterChain 의 permitAll()의 차이점]
 *  SecurityFilterChain 의 permitAll()은 보안 필터를 거치지만
 *  WebSecurityCustomizer 의 ignoring() 은 보안 필터를 거치지 않습니다.
 */
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authz -> authz
                        .mvcMatchers("/", "/users").permitAll()
                        .mvcMatchers("/mypage").hasRole("USER")
                        .mvcMatchers("/messages").hasRole("MANAGER")
                        .mvcMatchers("/config").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin()
        ;
        return http.build();
    }
}
