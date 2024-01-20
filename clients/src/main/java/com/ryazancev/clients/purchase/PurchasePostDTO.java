package com.ryazancev.clients.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasePostDTO {

    private Long customerId;
    private Long productId;
}
