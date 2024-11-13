package org.example.msvcauth.config;


import org.example.msvcauth.authentication.AuthenticationProviderManager;
import org.example.msvcauth.authentication.JWTAuthFilter;
import org.example.msvcauth.authentication.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthenticationProviderManager authenticationProviderManager;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;


    public SecurityConfig(AuthenticationProviderManager authenticationProviderManager, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.authenticationProviderManager = authenticationProviderManager;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/graphql/login").permitAll()
                        .requestMatchers("/graphql/**", "/graphiql/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilter(new JWTAuthFilter(authenticationProviderManager))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // Usamos BCryptPasswordEncoder
//    }
}