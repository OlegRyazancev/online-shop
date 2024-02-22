package com.ryazancev.dto.organization;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.customer.CustomerDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Organization DTO. Used in GET methods")
public class OrganizationDto {

    @Schema(
            description = "Organization ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the organization",
            example = "Example Organization"
    )
    private String name;

    @Schema(
            description = "Description of the organization",
            example = "This is a sample organization" +
                    " providing excellent services"
    )
    private String description;

    @Schema(
            description = "String logo of organization provided bt minio",
            example = "logo.png"
    )
    private String logo;

    @Schema(description = "Details of the organization's owner")
    private CustomerDto owner;

    @Schema(
            description = "Status of the organization (e.g., ACTIVE, INACTIVE)",
            example = "ACTIVE"
    )
    private String status;

    @Schema(
            description = "Date and time when the organization was registered",
            example = "2021-05-22 22:15"
    )
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registeredAt;
}
