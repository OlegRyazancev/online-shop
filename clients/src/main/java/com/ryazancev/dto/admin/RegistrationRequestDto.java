package com.ryazancev.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.admin.enums.ObjectType;
import com.ryazancev.dto.admin.enums.RequestStatus;
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
public class RegistrationRequestDto {

    private Long id;

    @NotNull(groups = OnCreate.class)
    private Long objectToRegisterId;

    @NotNull(groups = OnCreate.class)
    private ObjectType objectType;

    private RequestStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

}
