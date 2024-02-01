package com.ryazancev.auth.security.jwt;


import com.ryazancev.auth.dto.JwtResponse;
import com.ryazancev.auth.model.Role;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.auth.util.exception.custom.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserService userService;

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long userId,
                                    String email,
                                    Long customerId,
                                    Set<Role> roles) {

        Claims claims = Jwts.claims().setSubject(email);

        claims.put("id", userId);
        claims.put("customerId", customerId);
        claims.put("roles", resolveRoles(roles));

        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId,
                                     String email,
                                     Long customerId) {

        Claims claims = Jwts.claims().setSubject(email);

        claims.put("id", userId);
        claims.put("customerId", customerId);

        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {

        JwtResponse jwtResponse = new JwtResponse();

        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }

        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);

        jwtResponse.setId(userId);
        jwtResponse.setEmail(user.getEmail());
        jwtResponse.setCustomerId(user.getCustomerId());
        jwtResponse.setAccessToken(
                createAccessToken(
                        userId,
                        user.getEmail(),
                        user.getCustomerId(),
                        user.getRoles()
                ));
        jwtResponse.setRefreshToken(
                createRefreshToken(
                        userId,
                        user.getEmail(),
                        user.getCustomerId()
                ));

        return jwtResponse;
    }

    private List<String> resolveRoles(Set<Role> roles) {

        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
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

    private String getId(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }
}
