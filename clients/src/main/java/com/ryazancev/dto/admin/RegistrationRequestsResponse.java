package com.ryazancev.dto.admin;

import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestsResponse {

    private List<RegistrationRequestDTO> requests;
}
