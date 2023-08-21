package com.example.jwtdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    // Injecting JwtAuthFilter and AuthenticationProvider
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // Constructor injection for JwtAuthFilter and AuthenticationProvider
    public SecurityConfiguration(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }



    // configure secirity filter chain
    /**
     * Configures the security filter chain for the application.
     *
     * @param http The HttpSecurity instance to be configured.
     * @return A SecurityFilterChain that defines the security filters and rules.
     * @throws Exception In case of configuration errors.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        // Disable Cross-Site Request Forgery (CSRF) protection
        // Configure authorization rules for various URL patterns
    http.csrf()
                .disable()// Disable CSRF protection
                .authorizeHttpRequests()// Configure authorization rules for requests
                .requestMatchers("/api/v1/auth/register").permitAll()// Allow unauthenticated access to register
                .requestMatchers("/api/v1/auth/authenticate").permitAll()// Allow unauthenticated access to register
                .anyRequest()
                .authenticated()// Require authentication for any other request
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // we don't want to create a session
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
