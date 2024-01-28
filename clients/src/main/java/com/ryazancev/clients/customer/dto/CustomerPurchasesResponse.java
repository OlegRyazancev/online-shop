package com.ryazancev.clients.customer.dto;


import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPurchasesResponse {

    private List<PurchaseDTO> purchases;
}
