package com.ryazancev.clients.purchase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasePostDTO {

    @NotNull(message = "Customer ID must be not null")
    private Long customerId;

    @NotNull(message = "Product ID must be not null")
    private Long productId;
}
