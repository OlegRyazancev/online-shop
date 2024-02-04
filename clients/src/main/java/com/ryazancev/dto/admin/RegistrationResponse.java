package com.ryazancev.dto.admin;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegistrationResponse {

    private Long objectToBeRegisteredId;
    private ResponseStatus status;
}
