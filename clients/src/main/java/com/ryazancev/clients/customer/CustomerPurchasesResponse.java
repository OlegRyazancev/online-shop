package com.ryazancev.clients.customer;


import com.ryazancev.clients.purchase.PurchaseDTO;
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
