package com.ryazancev.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response containing a list of registration requests")
public class RegistrationRequestsResponse {

    @Schema(description = "List of registration requests")
    private List<RegistrationRequestDto> requests;
}
