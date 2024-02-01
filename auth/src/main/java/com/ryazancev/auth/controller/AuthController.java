package com.ryazancev.auth.controller;


import com.ryazancev.auth.dto.JwtRequest;
import com.ryazancev.auth.dto.JwtResponse;
import com.ryazancev.auth.dto.UserDTO;
import com.ryazancev.auth.service.AuthService;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.validation.OnCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDTO register(
            @Validated(OnCreate.class)
            @RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(
            @RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @GetMapping("/confirm")
    public String confirm(
            @RequestParam("token") String token){
        return confirmationTokenService.confirm(token);
    }

}
