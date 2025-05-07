package com.example.demo.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.model.Users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username,Users userlogin, Map<String, Object> customClaims) {
        long expirationTimeMs = 1000 * 60 * 1; 
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userlogin.getUserId());
        claims.put("email", userlogin.getEmail());
        claims.put("role", userlogin.getRole());
        
        if (customClaims != null) {
            claims.putAll(customClaims); // Merge the custom claims
        }
       
        return Jwts.builder()
        		.setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

//    public String extractUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//    
//    public Integer extractUserId(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("userId", Integer.class);
//    }
    public Map<String, Object> extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    


//    public boolean validateToken(String token, String username, Integer userId) {
//        try {
//            return username.equals(extractUsername(token)) &&
//                   userId.equals(extractUserId(token)) &&
//                   !isTokenExpired(token);
//        } catch (Exception e) {
//            return false;
//        }
//    }
    
	public boolean validateToken(String token, String expectedUsername,
            Integer expectedUserId,
            String expectedEmail,
            String expectedRole
			) {
		try {
			Map<String, Object> claims = extractAllClaims(token);

			String username = (String) claims.get("sub"); 
			Integer userId = (Integer) claims.get("userId");
			String email = (String) claims.get("email");
			String role = (String) claims.get("role");
			

			return expectedUsername.equals(username) && expectedUserId.equals(userId) && expectedEmail.equals(email)
					&& expectedRole.equals(role) 
					&& !isTokenExpired(token);

		} catch (Exception e) {
			return false;
		}
	}



    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
