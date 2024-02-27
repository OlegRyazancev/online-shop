package com.ryazancev.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author Oleg Ryazancev
 */

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.secret}")
    private String secret;


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String token) {

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);

        return !claimsJws
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String extractEmail(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public String extractId(String token) {

        return extractClaim(
                token,
                claims -> claims.get("id", Long.class)
        ).toString();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(
                token,
                claims -> claims.get("roles", List.class)
        );
    }

    public String extractLocked(String token) {

        return extractClaim(
                token,
                claims -> claims.get("locked", Boolean.class)
        ).toString();
    }

    public String extractConfirmed(String token) {

        return extractClaim(
                token,
                claims -> claims.get("confirmed", Boolean.class)
        ).toString();
    }


    public <T> T extractClaim(String token,
                              Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
