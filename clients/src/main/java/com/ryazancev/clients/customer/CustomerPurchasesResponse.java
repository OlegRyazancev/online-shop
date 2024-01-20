package com.ryazancev.clients.customer;


import com.ryazancev.clients.purchase.PurchaseDetailedDTO;
import com.ryazancev.clients.purchase.PurchasePostDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPurchasesResponse {

    private List<PurchaseDetailedDTO> purchases;
}
