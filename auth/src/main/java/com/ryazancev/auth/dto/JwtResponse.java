package com.ryazancev.auth.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private Long id;
    private String email;
    private String accessToken;
    private String refreshToken;
}