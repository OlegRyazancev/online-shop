package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.dto.JwtRequest;
import com.ryazancev.auth.dto.JwtResponse;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.security.jwt.JwtTokenProvider;
import com.ryazancev.auth.service.AuthService;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.auth.util.exception.custom.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {

        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        User user = userService
                .getByEmail(loginRequest.getEmail());

        if (user.getDeletedAt() != null) {

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm");
            String formattedDate = user.getDeletedAt().format(formatter);

            throw new AccessDeniedException(
                    String.format(
                            "Access denied. Sorry %s, but this " +
                                    "account was deleted at %s",
                            user.getEmail(),
                            formattedDate), HttpStatus.FORBIDDEN);
        }

        jwtResponse.setId(user.getId());
        jwtResponse.setEmail(user.getEmail());
        jwtResponse.setCustomerId(user.getCustomerId());
        jwtResponse.setLocked(user.isLocked());
        jwtResponse.setConfirmed(user.isConfirmed());
        jwtResponse.setAccessToken(
                jwtTokenProvider.createAccessToken(
                        user.getId(),
                        user.getEmail(),
                        user.getCustomerId(),
                        user.isLocked(),
                        user.isConfirmed(),
                        user.getRoles()));
        jwtResponse.setRefreshToken(
                jwtTokenProvider.createRefreshToken(
                        user.getId(),
                        user.getEmail(),
                        user.getCustomerId(),
                        user.isLocked(),
                        user.isConfirmed()));

        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {

        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
