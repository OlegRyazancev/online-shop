package com.ryazancev.dto.customer;


import com.ryazancev.dto.purchase.PurchaseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPurchasesResponse implements Serializable {

    private List<PurchaseDTO> purchases;
}
