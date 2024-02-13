package com.ryazancev.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectRequest {

    @NotNull(message = "Id must be not null")
    Long objectId;

    @NotNull(message = "Object type must be not null")
    ObjectType objectType;

    @NotNull(message = "Object status must be not null")
    ObjectStatus objectStatus;
}
