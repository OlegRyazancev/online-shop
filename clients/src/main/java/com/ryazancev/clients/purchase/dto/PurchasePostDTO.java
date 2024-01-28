package com.ryazancev.clients.purchase.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasePostDTO {

    private Long customerId;
    private Long productId;
}
