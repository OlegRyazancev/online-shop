package com.ryazancev.dto.customer;


import com.ryazancev.dto.purchase.PurchaseDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPurchasesResponse {

    private List<PurchaseDto> purchases;
}
