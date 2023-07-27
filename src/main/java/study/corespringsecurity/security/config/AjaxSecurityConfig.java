package study.corespringsecurity.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import study.corespringsecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import study.corespringsecurity.security.handler.AjaxAccessDeniedHandler;
import study.corespringsecurity.security.handler.AjaxAuthenticationFailureHandler;
import study.corespringsecurity.security.handler.AjaxAuthenticationSuccessHandler;
import study.corespringsecurity.security.provider.AjaxAuthenticationProvider;

@EnableWebSecurity
@RequiredArgsConstructor
@Order(0)
public class AjaxSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;

    @Bean
    public SecurityFilterChain ajaxSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authz -> authz
                        .mvcMatchers("/api/messages").hasRole("MANAGER")
                        .mvcMatchers("/api/message").hasRole("USER")
                        .mvcMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf((csrf) -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                        .accessDeniedHandler(ajaxAccessDeniedHandler())
                )
        ;
        customConfigurerAjax(http);
        return http.build();
    }

    private void customConfigurerAjax(HttpSecurity http) throws Exception {
        http
                .apply(new AjaxLoginConfigurer<>())
                .successHandlerAjax(ajaxAuthenticationSuccessHandler())
                .failureHandlerAjax(ajaxAuthenticationFailureHandler())
                .setAuthenticationManager(authenticationManager(authenticationConfiguration))
                .loginProcessingUrl("/api/login")
        ;
    }

    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler() {
        return new AjaxAccessDeniedHandler();
    }

//    @Bean
//    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
//        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
//        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
//        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
//        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
//        return ajaxLoginProcessingFilter;
//    }

    /**
     *  AuthenticationManager는 초기화 때 생성되어 기본적으로 DaoAuthenticationProvider 와 같은 객체를 가지고 있습니다.
     *  그리고 UsernamePasswordAuthenticationFilter 와 같은 클래스에서 참조하고 있습니다.
     *  그렇다면 ajaxAuthenticationProvider 도 초기화때 생성된 AuthenticationManager 에서 추가해주어야 합니다.
     *
     *  <div>아래의 코드가 초기화 때 생성된 AuthenticationManager 입니다.</div>
     *  <code>
     *      @Bean
     *      public AuthenticationManager formAuthManager(HttpSecurity http) throws Exception {
     *          AuthenticationManagerBuilder authenticationManagerBuilder =
     *                  http.getSharedObject(AuthenticationManagerBuilder.class);
     *          authenticationManagerBuilder.authenticationProvider(ajaxAuthenticationProvider);
     *          return authenticationManagerBuilder.build();
     *      }
     *  </code>
     *  <div>그런데 실제 AjaxLoginProcessingFilter를 생성하는 코드를 보면
     *  초기화때 생성된 AuthenticationManager 가 아닌 다른 AuthenticationManager 를 참조하고 있습니다.</div>
     *  <code>
     *      @Bean
     *      public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
     *          AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
     *          ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
     *          return ajaxLoginProcessingFilter;
     *      }
     *  </code>
     *  <div>
     *  즉, authenticationConfiguration.getAuthenticationManager(); 에서 참조하고 있는 AuthenticationManager 와
     *  authenticationManagerBuilder.authenticationProvider(ajaxAuthenticationProvider()); 를 통해 참조되는
     *  AuthenticationManager 는 동일한 객체가 아닙니다.
     *  그렇기 때문에 AjaxLoginProcessingFilter 에서 참조하고 있는 AuthenticationManager 에
     *  ajaxAuthenticationProvider 를 추가해 주어야 정상동작하게 됩니다.
     *  </div>
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ProviderManager authenticationManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        authenticationManager.getProviders().add(ajaxAuthenticationProvider);
        return authenticationManager;
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }
}
