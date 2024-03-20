package com.ryazancev.common.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
        description = "Response containing a list of "
                + "simplified organization DTOs"
)
public class OrganizationsSimpleResponse {

    @Schema(description = "List of organization DTOs")
    private List<OrganizationDto> organizations;
}
