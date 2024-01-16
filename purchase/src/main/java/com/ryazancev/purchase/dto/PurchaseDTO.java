package com.ryazancev.purchase.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDTO {

    private Long userId;
    private Long productId;
    private Double amount;
}
