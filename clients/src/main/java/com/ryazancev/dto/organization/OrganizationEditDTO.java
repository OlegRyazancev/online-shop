package com.ryazancev.dto.organization;


import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationEditDTO {

    @NotNull(message = "Id must be not null",
            groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Name must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Description must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 10,
            max = 500,
            message = "Description must be between 10 and 500 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "Owner ID must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    private Long ownerId;

}
