package com.example.demo.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.entity.UserToken;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;



@Component
public class JwtUtil {

    private final static String SECRET = "asdfghjklpoiuytrewqzxcvbnmkljhgfdasqwertyuiy";
    @Autowired
    private UserTokenRepository tokenRepository;
	private Key SECRET_KEY;
	private final long expirationTimeMs = 1000 * 60 * 3 ;
	@PostConstruct
	public void init() {
		this.SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
	}
	
    public String generateToken(String username,Users userlogin, Map<String, Object> customClaims) {
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userlogin.getUserId());
        claims.put("email", userlogin.getEmail());
        claims.put("role", userlogin.getRole());
        
		if (customClaims != null) {
			claims.putAll(customClaims);
		}

		String token = Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(now).setExpiration(expiryDate)
				.signWith(SECRET_KEY).compact();

		Boolean existsByUsername = tokenRepository.existsByUsername(username);
		if (existsByUsername) {
			Optional<UserToken> byUsername = tokenRepository.findByUsername(username);
			UserToken userToken = byUsername.get();
			userToken.setToken(token);
			userToken.setExpiryDate(extractExpiration(token));
			tokenRepository.save(userToken);
		} else {
			UserToken newtoken = new UserToken();
			System.out.println(" Generating new token...");
			newtoken.setToken(token);
			newtoken.setUsername(username);
			newtoken.setExpiryDate(extractExpiration(token));
			tokenRepository.save(newtoken);
		}

		return token;
	}

//    public String getOrCreateToken(Users userLogin) {
//        String username = userLogin.getUsername();
//        Optional<UserToken> userToken = tokenRepository.findByUsername(username);
//
//        if (userToken.isPresent()) {
//            UserToken tokenEntity = userToken.get();
//            if (!isTokenExpired(tokenEntity.getToken())) {
//            	System.out.println("token not expired");
//            	System.out.println("11111");
//                return tokenEntity.getToken();
//            } else {
//            	System.out.println("222222");
//                String newToken = generateToken(username, userLogin, null);
//                tokenEntity.setToken(newToken);
//                
//                tokenRepository.save(tokenEntity);
//                System.out.println("new token updated");
//                
//                return newToken;
//            }
//        } else {
//        	System.out.println("333333");
//            String token = generateToken(username, userLogin, null);
//            UserToken newToken = new UserToken();
//            newToken.setUsername(username);
//            newToken.setToken(token);
//            newToken.setExpiryDate(extractExpiration(token));
//            tokenRepository.save(newToken);
//            
//
//            return token;
//        }
//    }
    
//    public String getOrCreateToken(Users user) {
//        String username = user.getUsername();
//        Optional<UserToken> userToken = tokenRepository.findByUsername(username);
//
//      
//            	 UserToken token = userToken.get();
//                System.out.println("Token is expired. Generating new token...");
//                String newToken = generateToken(username, user, null);
////                token.setToken(newToken);
////                token.setExpiryDate(extractExpiration(newToken));
////                
////                tokenRepository.save(token);
//                return newToken;
//            }
//        
    

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