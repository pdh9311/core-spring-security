package study.corespringsecurity.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import study.corespringsecurity.security.common.FormAuthenticationDetailsSource;
import study.corespringsecurity.security.handler.FormAccessDeniedHandler;
import study.corespringsecurity.security.handler.FormAuthenticationFailureHandler;
import study.corespringsecurity.security.handler.FormAuthenticationSuccessHandler;
import study.corespringsecurity.security.provider.FormAuthenticationProvider;

/**
 * [WebSecurityCustomizer 의 ignoring() 과 SecurityFilterChain 의 permitAll()의 차이점]
 * SecurityFilterChain 의 permitAll()은 보안 필터를 거치지만
 * WebSecurityCustomizer 의 ignoring() 은 보안 필터를 거치지 않습니다.
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig {

    private final FormAuthenticationProvider formAuthenticationProvider;
    private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;
    private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;

    @Bean
    public AuthenticationManager formAuthManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(formAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain formSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authz -> authz
                        .mvcMatchers("/", "/users", "/login*", "/api/login").permitAll()
                        .mvcMatchers("/mypage").hasRole("USER")
                        .mvcMatchers("/messages").hasRole("MANAGER")
                        .mvcMatchers("/config").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login_proc")
                        .defaultSuccessUrl("/")
                        .authenticationDetailsSource(formAuthenticationDetailsSource)
                        .successHandler(formAuthenticationSuccessHandler)
                        .failureHandler(formAuthenticationFailureHandler)
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                )
        ;
        return http.build();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        FormAccessDeniedHandler accessDeniedHandler = new FormAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }


}
