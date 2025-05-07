package com.example.demo.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username,Users userlogin, Map<String, Object> customClaims) {
        long expirationTimeMs = 1000 * 60 * 60; 
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userlogin.getUserId());
        claims.put("email", userlogin.getEmail());
        claims.put("role", userlogin.getRole());
        
        if (customClaims != null) {
            claims.putAll(customClaims); 
        }
       
        return Jwts.builder()
        		.setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }
    
    

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

   
	public boolean validateToken(String token, String expectedUsername, Long expectedUserId, String expectedEmail,
			String expectedRole) {
		try {
			Map<String, Object> claims = extractAllClaims(token);

			String username =extractUsername(token);
			Long userId =extractUserId(token);
			String email =extractEmail(token);
			String role = extractRole(token);
			
			
			System.out.println("Username: expected=" + expectedUsername + ", actual=" + username);
			System.out.println("UserId: expected=" + expectedUserId + ", actual=" + userId);
			System.out.println("Email: expected=" + expectedEmail + ", actual=" + email);
			System.out.println("Role: expected=" + expectedRole + ", actual=" + role);
			System.out.println("Token expired: " + isTokenExpired(token));

			
			return expectedUsername.equals(username) && expectedUserId.equals(userId) && expectedEmail.equals(email)
					&& expectedRole.equals(role) && !isTokenExpired(token);

		} catch (Exception e) {
			return false;
		}
	}


}
