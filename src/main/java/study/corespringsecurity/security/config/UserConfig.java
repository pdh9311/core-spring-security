package study.corespringsecurity.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {
/*
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        String password = passwordEncoder().encode("1234");

        UserDetails user = User.withUsername("user")
                .password(password)
                .roles("USER")
                .build();
        UserDetails manager = User.withUsername("manager")
                .password(password)
                .roles("MANAGER", "USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(password)
                .roles("ADMIN", "MANAGER", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, manager, admin);
    }
*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
