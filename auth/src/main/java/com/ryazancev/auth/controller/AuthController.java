package com.ryazancev.auth.controller;


import com.ryazancev.auth.dto.JwtRequest;
import com.ryazancev.auth.dto.JwtResponse;
import com.ryazancev.auth.service.AuthService;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.common.dto.user.UserDto;
import com.ryazancev.common.validation.OnCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {


    private final AuthService authService;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping("/login")
    public JwtResponse login(
            @Validated
            @RequestBody final JwtRequest loginRequest) {

        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(
            @Validated(OnCreate.class)
            @RequestBody final UserDto userDto) {

        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(
            @RequestBody final String refreshToken) {

        return authService.refresh(refreshToken);
    }

    @GetMapping("/confirm")
    public String confirm(
            @RequestParam("token") final String token) {

        return confirmationTokenService.confirm(token);
    }
}
