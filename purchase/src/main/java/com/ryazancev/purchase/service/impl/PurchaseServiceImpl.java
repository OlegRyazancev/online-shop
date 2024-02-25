package com.ryazancev.purchase.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.product.PriceQuantityResponse;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.PurchaseUtil;
import com.ryazancev.purchase.util.exception.custom.PurchaseNotFoundException;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import com.ryazancev.purchase.util.validator.PurchaseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.CircuitBreaker;
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
    private final PurchaseUtil purchaseUtil;

    private final ProductClient productClient;
    private final CustomerClient customerClient;

    @Override
    public PurchaseDto getById(String id) {

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() ->
                        new PurchaseNotFoundException(
                                "Purchase with this ID not found",
                                HttpStatus.NOT_FOUND));

        return purchaseUtil.createPurchaseDto(purchase);
    }

    //TODO:CB here
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

        Long productOwnerId = productClient.getOwnerId(productId);
        Double ownerBalance = customerClient.getBalanceById(productOwnerId);

        Purchase toSave = purchaseMapper.toEntity(purchaseEditDto);
        toSave.setPurchaseDate(LocalDateTime.now());
        toSave.setAmount(selectedProductPrice);

        purchaseUtil.updateCustomerBalance(customerId,
                availableCustomerBalance - selectedProductPrice);
        purchaseUtil.updateProductQuantity(productId,
                availableProductsInStock - 1);
        purchaseUtil.updateCustomerBalance(productOwnerId,
                ownerBalance + selectedProductPrice);

        Purchase saved = purchaseRepository.save(toSave);

        return purchaseUtil.createPurchaseDto(saved);
    }


    @Override
    public CustomerPurchasesResponse getByCustomerId(Long customerId) {
        List<Purchase> purchases = purchaseRepository
                .findByCustomerId(customerId);

        purchaseValidator.validatePurchasesExist(purchases);

        List<PurchaseDto> purchasesDto = purchaseUtil
                .enrichPurchasesWithProductInfo(purchases);

        return CustomerPurchasesResponse.builder()
                .purchases(purchasesDto)
                .build();
    }

}
