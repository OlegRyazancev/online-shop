package com.ryazancev.auth.service;


import com.ryazancev.auth.dto.JwtRequest;
import com.ryazancev.auth.dto.JwtResponse;

/**
 * @author Oleg Ryazancev
 */

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
