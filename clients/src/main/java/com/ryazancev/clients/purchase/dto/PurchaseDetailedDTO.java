package com.ryazancev.clients.purchase.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDetailedDTO {

    private String id;
    private Long customerId;
    private Long productId;
    private Double amount;
    private LocalDateTime purchaseDate;
}
