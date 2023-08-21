package com.example.jwtdemo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

//    private static final String SECRET_KEY = "MHQCAQEEIMuMojbVVKLKk/tcb+9c1oMdASxlXVZ9pB8mFw4zTAI9oAcGBSuBBAAK" +
//            "oUQDQgAEixVCpeX/ZD0bcvu1nTdxFFyeyGW87O1w2FfdB448hhNIxupLbvVFUC4P" +
//            "3Xkc/7Mtc2YILwViht3YupeI//hfnw==";

    // private key for signing the token
    private static final String SECRET_KEY = "secretkeyforthemainnameofthesidetandthkdndaebwetidlsgsdthsdiydntes ";

    ///

    // generate token
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }

    // generating a token
    public String generateToken(
            Map<String , Object> extractClaims,
            UserDetails userDetails
    ){
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *24 ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // Validate whether a token is valid for the given UserDetails
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if a token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // extract all claims method
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Get the signing key from the provided secret key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
