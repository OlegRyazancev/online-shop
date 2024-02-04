package com.ryazancev.dto.admin;


import lombok.*;


@Builder
@Data
public class RegistrationRequest {

    private Long objectToBeRegisteredId;

    private RequestType type;
}
