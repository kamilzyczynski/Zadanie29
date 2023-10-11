package com.example.zadanie29.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                .anyRequest().authenticated());
        http.csrf(config -> config.ignoringRequestMatchers(toH2Console()));
        http.formLogin(login -> login.loginPage("/login").permitAll());
        http.headers(
                config -> config.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin
                )
        );
        http.logout(
                config -> {
                    config.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));
                    config.logoutSuccessUrl("/");
                }
        );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
