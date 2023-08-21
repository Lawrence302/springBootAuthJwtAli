package com.example.jwtdemo.service;

import com.example.jwtdemo.Entity.Rols;
import com.example.jwtdemo.Entity.User;
import com.example.jwtdemo.auth.AuthenticationRequest;
import com.example.jwtdemo.auth.AuthenticationResponse;
import com.example.jwtdemo.auth.RegisterRequest;
import com.example.jwtdemo.exceptions.GlobalException;
import com.example.jwtdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    // for encoding password
    @Autowired
    private  PasswordEncoder passwordEncoder;

    // jwt service for jwt
    @Autowired
    private JwtService jwtService;

    // authentication manager
    @Autowired
    private AuthenticationManager authenticationManager;

    // method to register new user
    public AuthenticationResponse register(RegisterRequest request) {
        // Creating a new User entity and setting its properties
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRols(Rols.USER); // assigning default tole

        // checking if the email is already registered
        var userOptional = userRepository.findByEmail(request.getEmail());
        if(userOptional.isPresent()){
            throw new GlobalException("Email already registered");
        }

        // saving the new user entity to database
        userRepository.save(user);

        // Generating a JWT token for the registered user
        var jwtToken = jwtService.generateToken(user);

        // Returning an instance of AuthenticationResponse containing the JWT token
        return new AuthenticationResponse(jwtToken);

    }

    // method to authenticate a user
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticating the user's credentials using the authentication manager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );

        // If no exception is thrown, authentication was successful

        // finding the user by email id in the database
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        //generating jwt token for the authenticated  user
        var jwtToken = jwtService.generateToken(user);

        //// Returning an instance of AuthenticationResponse containing the JWT token
        return new AuthenticationResponse(jwtToken);
    }
}
