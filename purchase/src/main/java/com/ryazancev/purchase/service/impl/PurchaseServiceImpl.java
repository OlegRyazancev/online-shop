package com.ryazancev.purchase.service.impl;

import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.PurchaseUtil;
import com.ryazancev.purchase.util.exception.custom.PurchaseNotFoundException;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import com.ryazancev.purchase.util.validator.PurchaseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.ryazancev.purchase.util.exception.Message.PURCHASE_NOT_FOUND;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseValidator purchaseValidator;
    private final PurchaseUtil purchaseUtil;

    private final ClientsService clientsService;


    @Override
    public PurchaseDto getById(String id) {

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() ->
                        new PurchaseNotFoundException(
                                PURCHASE_NOT_FOUND,
                                HttpStatus.NOT_FOUND));

        return purchaseUtil.createPurchaseDto(purchase);
    }

    @Transactional
    @Override
    public PurchaseDto processPurchase(
            PurchaseEditDto purchaseEditDto) {

        Long customerId = purchaseEditDto.getCustomerId();
        Long productId = purchaseEditDto.getProductId();

        Double availableCustomerBalance = (Double) clientsService
                .getCustomerBalance(customerId);

        PriceQuantityResponse priceQuantityResponse =
                (PriceQuantityResponse) clientsService
                        .getProductPriceAndQuantity(productId);

        Long productOwnerId = (Long) clientsService
                .getProductOwnerId(productId);

        Double ownerBalance = (Double) clientsService
                .getCustomerBalance(productOwnerId);

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

        List<PurchaseDto> purchasesDto = purchases.isEmpty() ?
                Collections.emptyList()
                : purchaseUtil.createPurchasesDtoWithProductsInfo(purchases);

        return CustomerPurchasesResponse.builder()
                .purchases(purchasesDto)
                .build();
    }

}
