package com.ryazancev.common.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import com.ryazancev.common.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request model for manage registration of objects")
public class RegistrationRequestDto {


    @Schema(
            description = "Request ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "ID of the object to be registered",
            example = "1"
    )
    @NotNull(groups = OnCreate.class)
    private Long objectToRegisterId;

    @Schema(
            description = "Type of the object to be registered (e.g., PRODUCT)",
            example = "PRODUCT"
    )
    @NotNull(groups = OnCreate.class)
    private ObjectType objectType;

    @Schema(
            description = "Status of the registration request "
                    + "(e.g., ON_REVIEW)",
            example = "ON_REVIEW"
    )
    private RequestStatus status;

    @Schema(
            description = "Date when request was created",
            example = "2024-05-22T22:15:00"
    )
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(
            description = "Date when request was accepted/rejected",
            example = "2024-06-22T22:15"
    )
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reviewedAt;

}
