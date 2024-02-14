package com.ryazancev.dto.admin;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLockRequest {

    @NotNull(message = "Username must be not null")
    private String username;

    @NotNull(message = "Lock value must be nit null")
    private boolean lock;
}
