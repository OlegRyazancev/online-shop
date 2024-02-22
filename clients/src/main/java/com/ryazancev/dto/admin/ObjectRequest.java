package com.ryazancev.dto.admin;

import com.ryazancev.dto.admin.enums.ObjectStatus;
import com.ryazancev.dto.admin.enums.ObjectType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObjectRequest {

    @NotNull(message = "Id must be not null")
    Long objectId;

    @NotNull(message = "Object type must be not null")
    ObjectType objectType;

    @NotNull(message = "Object status must be not null")
    ObjectStatus objectStatus;
}
