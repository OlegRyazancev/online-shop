package com.ryazancev.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.admin.model.RequestStatus;
import com.ryazancev.validation.OnCreate;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgRegRequestDTO {

    private Long id;

    //on create
    @NotNull(message = "Organization id must not be null",
            groups = OnCreate.class)
    private Long organizationId;

    private RequestStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;
}
