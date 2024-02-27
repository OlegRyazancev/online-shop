package com.ryazancev.purchase.util;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.common.dto.product.UpdateQuantityRequest;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.purchase.kafka.PurchaseProducerService;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseUtil {

    private final PurchaseMapper purchaseMapper;
    private final PurchaseProducerService purchaseProducerService;

    private final ClientsService clientsService;


    public PurchaseDto createPurchaseDto(Purchase purchase) {

        PurchaseDto purchaseDto = purchaseMapper.toDto(purchase);

        purchaseDto.setCustomer(clientsService
                .getSimpleCustomerById(purchase.getCustomerId()));

        purchaseDto.setProduct(clientsService
                .getSimpleProductById(purchase.getProductId()));

        return purchaseDto;
    }

    public List<PurchaseDto> createPurchasesDtoWithProductsInfo(
            List<Purchase> purchases) {

        List<PurchaseDto> purchasesDto = purchaseMapper.toListDto(purchases);

        for (int i = 0; i < purchasesDto.size(); i++) {
            Long productId = purchases.get(i).getProductId();
            purchasesDto.get(i)
                    .setProduct(clientsService
                            .getSimpleProductById(productId));
        }

        return purchasesDto;
    }

    public void updateProductQuantity(Long productId,
                                      Integer availableProductsInStock) {

        purchaseProducerService.sendMessageToProductTopic(
                UpdateQuantityRequest.builder()
                        .productId(productId)
                        .quantityInStock(availableProductsInStock)
                        .build()
        );
    }

    public void updateCustomerBalance(Long customerId,
                                      Double updatedBalance) {

        purchaseProducerService.sendMessageToCustomerTopic(
                UpdateBalanceRequest.builder()
                        .customerId(customerId)
                        .balance(updatedBalance)
                        .build()
        );
    }
}
