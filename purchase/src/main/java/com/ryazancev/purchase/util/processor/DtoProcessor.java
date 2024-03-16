package com.ryazancev.purchase.util.processor;

import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DtoProcessor {

    private final PurchaseMapper purchaseMapper;
    private final ClientsService clientsService;

    public CustomerPurchasesResponse createCustomerPurchasesResponse(
            List<Purchase> purchases) {

        List<PurchaseDto> purchasesDto = Collections.emptyList();

        if (!purchases.isEmpty()) {

            purchasesDto = purchaseMapper.toListDto(purchases);

            for (int i = 0; i < purchasesDto.size(); i++) {
                purchasesDto.get(i).setProduct(
                        clientsService.getSimpleProductById(
                                purchases.get(i).getProductId()));
            }
        }

        return CustomerPurchasesResponse.builder()
                .purchases(purchasesDto)
                .build();
    }

    public PurchaseDto createPurchaseDto(Purchase purchase) {

        PurchaseDto purchaseDto = purchaseMapper.toDto(purchase);

        purchaseDto.setCustomer(clientsService
                .getSimpleCustomerById(purchase.getCustomerId()));

        purchaseDto.setProduct(clientsService
                .getSimpleProductById(purchase.getProductId()));

        return purchaseDto;
    }

}
