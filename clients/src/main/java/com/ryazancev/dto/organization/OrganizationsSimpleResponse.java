package com.ryazancev.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        description = "Response containing a list of " +
                "simplified organization DTOs"
)
public class OrganizationsSimpleResponse {

    @Schema(description = "List of organization DTOs")
    private List<OrganizationDto> organizations;
}
