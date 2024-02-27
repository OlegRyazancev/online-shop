package com.ryazancev.common.dto.customer;


import com.ryazancev.common.dto.purchase.PurchaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response model for customer's purchases")
public class CustomerPurchasesResponse {

    @Schema(description = "List of customer's purchases")
    private List<PurchaseDto> purchases;
}
