package com.notesApp.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    // 🔹 Generate JWT token
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    // 🔹 Create JWT
    private String createToken(Map<String, Object> claims, String subject) {
        Key key = getSignKey();
        return Jwts.builder()
                .claims(claims)  // ✅ Fixed: Use .claims() instead of .setClaims()
                .subject(subject)  // ✅ Fixed: Use .subject() instead of .setSubject()
                .issuedAt(new Date(System.currentTimeMillis()))  // ✅ Fixed
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))  // ✅ Fixed
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Extract username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 🔹 Extract expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🔹 Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 🔹 Validate token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 🔹 Check expiration
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 🔹 Extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignKey())  // ✅ Fixed: Use .verifyWith()
                .build()
                .parseSignedClaims(token)  // ✅ Fixed: Use .parseSignedClaims()
                .getPayload();  // ✅ Fixed: Use .getPayload() instead of .getBody()
    }

    // 🔹 Decode secret key
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}

