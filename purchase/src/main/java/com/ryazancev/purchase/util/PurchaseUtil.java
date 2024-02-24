package com.ryazancev.purchase.util;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.product.ProductDto;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.purchase.kafka.PurchaseProducerService;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseUtil {

    private final PurchaseMapper purchaseMapper;
    private final PurchaseProducerService purchaseProducerService;

    private final CustomerClient customerClient;
    private final ProductClient productClient;


    public PurchaseDto createPurchaseDto(Purchase purchase) {

        PurchaseDto purchaseDto = purchaseMapper.toDto(purchase);

        purchaseDto.setCustomer(
                customerClient.getSimpleById(purchase.getCustomerId()));
        purchaseDto.setProduct(
                productClient.getSimpleById(purchase.getProductId()));

        return purchaseDto;
    }

    public List<PurchaseDto> enrichPurchasesWithProductInfo(
            List<Purchase> purchases) {

        List<PurchaseDto> purchasesDto = purchaseMapper.toListDto(purchases);

        for (int i = 0; i < purchasesDto.size(); i++) {
            ProductDto productDto = productClient
                    .getSimpleById(purchases.get(i).getProductId());
            purchasesDto.get(i).setProduct(productDto);
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
