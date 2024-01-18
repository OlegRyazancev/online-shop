package com.ryazancev.clients;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseDTO {

    private Long customerId;
    private Long productId;
    private Double amount;
    private LocalDateTime purchaseDate;
}
