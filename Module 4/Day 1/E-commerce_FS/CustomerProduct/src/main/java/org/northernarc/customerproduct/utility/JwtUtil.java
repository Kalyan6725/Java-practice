package org.northernarc.customerproduct.utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
@Component
public class JwtUtil {
    // Must be at least 256 bits (32 characters) long
    private static final String SECRET_STRING = "your-super-secret-key-that-is-at-least-32-characters-long!";
    private final SecretKey SEC_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 Hour
    
    // Generate token with username only (deprecated - use generateToken with role)
    public String generateToken(String username) {
        return generateToken(username, "USER");
    }
    
    // Generate token with username and role
    public String generateToken(String username, String role) {
        Map<String, Object> additionalClaims = new HashMap<>();
        // Add role with ROLE_ prefix for Spring Security compatibility
        additionalClaims.put("roles", List.of("ROLE_" + role));
        
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claims(additionalClaims)
                .signWith(SEC_KEY)
                .compact();
    }
    public String extractUsername(String token) {
        Claims claims =extractAllClaims(token);
        return claims.getSubject();
    }
    private Date extractExpiryDate(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }
    public boolean isTokenExpired(String token) {
        return extractExpiryDate(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SEC_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}