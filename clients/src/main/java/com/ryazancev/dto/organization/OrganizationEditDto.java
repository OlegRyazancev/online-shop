package com.ryazancev.dto.organization;


import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Organization Edit DTO, used for PUT, POST, DELETE")
public class OrganizationEditDto {

    @Schema(
            description = "Organization ID",
            example = "1"
    )
    @NotNull(
            message = "Id must be not null",
            groups = OnUpdate.class
    )
    private Long id;

    @Schema(
            description = "Name of the organization",
            example = "Example Organization"
    )
    @NotNull(
            message = "Name must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

    @Schema(
            description = "Description of the organization",
            example = "This is a sample organization " +
                    "providing excellent services"
    )
    @NotNull(
            message = "Description must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            min = 10,
            max = 500,
            message = "Description must be between 10 and 500 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String description;

    @Schema(
            description = "ID of the organization's owner.",
            example = "1"
    )
    @NotNull(
            message = "Owner ID must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Long ownerId;

}
