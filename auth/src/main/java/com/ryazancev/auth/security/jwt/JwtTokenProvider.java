package com.ryazancev.auth.security.jwt;


import com.ryazancev.auth.dto.JwtResponse;
import com.ryazancev.auth.model.Role;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.auth.util.exception.CustomExceptionFactory;
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

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserService userService;

    private Key getSignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(final Long userId,
                                    final String email,
                                    final Long customerId,
                                    final boolean isLocked,
                                    final boolean isConfirmed,
                                    final Set<Role> roles) {

        Claims claims = Jwts.claims().setSubject(email);

        claims.put("id", userId);
        claims.put("customerId", customerId);
        claims.put("locked", isLocked);
        claims.put("confirmed", isConfirmed);
        claims.put("roles", resolveRoles(roles));

        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(final Long userId,
                                     final String email,
                                     final Long customerId,
                                     final boolean isLocked,
                                     final boolean isConfirmed) {

        Claims claims = Jwts.claims().setSubject(email);

        claims.put("id", userId);
        claims.put("customerId", customerId);
        claims.put("locked", isLocked);
        claims.put("confirmed", isConfirmed);

        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtResponse refreshUserTokens(final String refreshToken) {

        JwtResponse jwtResponse = new JwtResponse();

        if (!validateToken(refreshToken)) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .invalidRefresh();
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
                        user.isLocked(),
                        user.isConfirmed(),
                        user.getRoles()
                ));
        jwtResponse.setRefreshToken(
                createRefreshToken(
                        userId,
                        user.getEmail(),
                        user.getCustomerId(),
                        user.isLocked(),
                        user.isConfirmed()
                ));

        return jwtResponse;
    }

    private List<String> resolveRoles(final Set<Role> roles) {

        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public boolean validateToken(final String token) {

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);

        return !claimsJws
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    private String getId(final String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }
}
