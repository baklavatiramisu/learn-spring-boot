package com.baklavatiramisu.learn.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSecurityConfig {
    @Bean
    public SecurityFilterChain applicationSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz

                        .requestMatchers(HttpMethod.GET, "/users/*/statuses").hasRole("status:read")
                        .requestMatchers(HttpMethod.GET, "/users/*/statuses/*").hasRole("status:read")
                        .requestMatchers(HttpMethod.POST, "/users/*/statuses").hasRole("status:write")
                        .requestMatchers(HttpMethod.PUT, "/users/*/statuses/*").hasRole("status:write")
                        .requestMatchers(HttpMethod.DELETE, "/users/*/statuses/*").hasRole("status:write")

                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("user:read")
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("admin")
                        .requestMatchers(HttpMethod.PUT, "/users/*").hasRole("user:write")
                        .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("admin")
                        .anyRequest().authenticated())
                .csrf(configurer -> configurer.disable())
                .build();
    }
}
