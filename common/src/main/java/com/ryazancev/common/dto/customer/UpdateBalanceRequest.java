package com.ryazancev.common.dto.customer;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Request model to change customer's balance")
public class UpdateBalanceRequest {

    @Schema(
            description = "Customer ID",
            example = "1"
    )
    private Long customerId;

    @Schema(
            description = "New balance",
            example = "10000"
    )
    private Double balance;
}
