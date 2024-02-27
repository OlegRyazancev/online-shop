package com.ryazancev.common.dto.admin;

import com.ryazancev.common.dto.admin.enums.ObjectStatus;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request model for change " +
        "object statuses in admin service")
public class ObjectRequest {


    @Schema(
            description = "Object ID",
            example = "1"
    )
    @NotNull(message = "Id must be not null")
    Long objectId;

    @Schema(
            description = "Type of the object (e.g., PRODUCT, ORGANIZATION)",
            example = "ORGANIZATION"
    )
    @NotNull(message = "Object type must be not null")
    ObjectType objectType;

    @Schema(
            description = "Status of the object (e.g., ACTIVATE, FREEZE)",
            example = "FREEZE"
    )
    @NotNull(message = "Object status must be not null")
    ObjectStatus objectStatus;
}
