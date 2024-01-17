package com.ryazancev.clients.purchase.dto;


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
