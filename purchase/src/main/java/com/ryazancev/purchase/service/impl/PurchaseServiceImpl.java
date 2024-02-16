package com.ryazancev.purchase.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
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
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import com.ryazancev.purchase.util.validator.PurchaseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PurchaseValidator purchaseValidator;

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

        purchaseValidator.validateSufficientBalance(
                availableCustomerBalance,
                selectedProductPrice);
        purchaseValidator.validateProductAvailability(
                availableProductsInStock);
        //todo: for future add percent to admin

        updateCustomerBalance(customerId,
                availableCustomerBalance - selectedProductPrice);
        updateProductQuantity(productId,
                availableProductsInStock - 1);

        Long productOwnerId = productClient.getOwnerId(productId);
        Double ownerBalance = customerClient.getBalanceById(productOwnerId);

        updateCustomerBalance(productOwnerId,
                ownerBalance + selectedProductPrice);

        Purchase toSave = purchaseMapper.toEntity(purchaseEditDto);
        toSave.setPurchaseDate(LocalDateTime.now());
        toSave.setAmount(selectedProductPrice);

        Purchase saved = purchaseRepository.save(toSave);

        return createPurchaseDto(saved);
    }

    private PurchaseDto createPurchaseDto(Purchase saved) {

        PurchaseDto purchaseDto = purchaseMapper.toDto(saved);

        purchaseDto.setCustomer(
                customerClient.getSimpleById(saved.getCustomerId()));
        purchaseDto.setProduct(
                productClient.getSimpleById(saved.getProductId()));

        return purchaseDto;
    }

    private void updateProductQuantity(Long productId,
                                       Integer availableProductsInStock) {

        purchaseProducerService.sendMessageToProductTopic(
                UpdateQuantityRequest.builder()
                        .productId(productId)
                        .quantityInStock(availableProductsInStock)
                        .build()
        );
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

    @Override
    public CustomerPurchasesResponse getByCustomerId(Long customerId) {
        List<Purchase> purchases = purchaseRepository
                .findByCustomerId(customerId);

        purchaseValidator.validatePurchasesExist(purchases);

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
}
