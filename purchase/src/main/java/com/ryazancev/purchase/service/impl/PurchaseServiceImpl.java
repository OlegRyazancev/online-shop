package com.ryazancev.purchase.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.product.PriceQuantityResponse;
import com.ryazancev.dto.product.ProductDto;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.kafka.PurchaseProducerService;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.exception.custom.IncorrectBalanceException;
import com.ryazancev.purchase.util.exception.custom.OutOfStockException;
import com.ryazancev.purchase.util.exception.custom.PurchasesNotFoundException;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    private final ProductClient productClient;
    private final CustomerClient customerClient;

    private final PurchaseProducerService purchaseProducerService;

    @Transactional
    @Override
    public PurchaseDto processPurchase(
            PurchaseEditDto purchaseEditDto) {

        Long customerId = purchaseEditDto.getCustomerId();
        Long productId = purchaseEditDto.getProductId();

        Double availableCustomerBalance = customerClient
                .getBalanceById(customerId);

       PriceQuantityResponse priceQuantityResponse = productClient
                .getPriceAndQuantityByProductId(productId);
        Double selectedProductPrice = 
                priceQuantityResponse.getPrice();
        Integer availableProductsInStock = 
                priceQuantityResponse.getQuantityInStock();

        if (availableCustomerBalance < selectedProductPrice) {
            throw new IncorrectBalanceException(
                    "Customer doesn't have enough money " +
                            "to purchase the product",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (availableProductsInStock == 0) {
            throw new OutOfStockException(
                    "No available products in stock",
                    HttpStatus.CONFLICT
            );
        }

        //todo: increase money in organization's owner (for future add percent to admin)

        updateCustomerBalance(customerId,
                availableCustomerBalance - selectedProductPrice);
        updateProductQuantity(productId, availableProductsInStock);

        Purchase toSave = purchaseMapper.toEntity(purchaseEditDto);
        toSave.setPurchaseDate(LocalDateTime.now());
        toSave.setAmount(selectedProductPrice);

        Purchase saved = purchaseRepository.save(toSave);
        PurchaseDto purchaseDto = purchaseMapper.toDto(saved);

        CustomerDto customerDto = customerClient.getSimpleById(customerId);
        ProductDto productDto = productClient.getSimpleById(productId);

        purchaseDto.setCustomer(customerDto);
        purchaseDto.setProduct(productDto);

        return purchaseDto;
    }

    private void updateProductQuantity(Long productId,
                                       Integer availableProductsInStock) {

        purchaseProducerService.sendMessageToProductTopic(
                UpdateQuantityRequest.builder()
                        .productId(productId)
                        .quantityInStock(availableProductsInStock - 1)
                        .build()
        );
    }

    @Override
    public CustomerPurchasesResponse getByCustomerId(Long customerId) {
        List<Purchase> purchases = purchaseRepository
                .findByCustomerId(customerId);

        if (purchases.isEmpty()) {
            throw new PurchasesNotFoundException(
                    "No purchases found for customer with this ID",
                    HttpStatus.NOT_FOUND
            );
        }

        List<PurchaseDto> purchasesDto = purchaseMapper.toListDto(purchases);

        for (int i = 0; i < purchasesDto.size(); i++) {
            ProductDto productDto = productClient
                    .getSimpleById(purchases.get(i).getProductId());
            purchasesDto.get(i).setProduct(productDto);
        }
        return CustomerPurchasesResponse.builder()
                .purchases(purchasesDto)
                .build();
    }

    private void updateCustomerBalance(Long customerId,
                                       Double updatedBalance) {

        purchaseProducerService.sendMessageToCustomerTopic(
                UpdateBalanceRequest.builder()
                        .customerId(customerId)
                        .balance(updatedBalance)
                        .build()
        );
    }

}
