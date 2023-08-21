package com.example.jwtdemo.config;

import com.example.jwtdemo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    // UserDetailsService is an interface provided by Spring Security.
    // We create our own implementation of the interface to properly manage it.
    private final UserDetailsService userDetailsService;

    // Constructor injection to provide the UserDetailsService implementation.
    public JwtAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //The method starts by extracting the "Authorization" header from the incoming HTTP request,
        // which is where the JWT token should be provided.
        final String authHeader = request.getHeader("Authorization"); // The authorization token is found in the header
        final String jwt;
        final String userEmail; // User's email extracted from the JWT token

        // Checking that the header is not empty and starts with "Bearer"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If no token or token doesn't start with "Bearer", continue to the next filter
            filterChain.doFilter(request, response);
            return;
        }

        // Extracting the token from the header (removing "Bearer ")
        jwt = authHeader.substring(7);

        // Extracting user email from JWT token
        userEmail = jwtService.extractUsername(jwt);

        // Checking if user email is not null and there's no existing authentication context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Validating the token using JwtService and UserDetails
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Creating an authentication token and updating the security context holder
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,  // No credentials (password) since it's a token-based authentication
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Updating the SecurityContextHolder with the authenticated token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
