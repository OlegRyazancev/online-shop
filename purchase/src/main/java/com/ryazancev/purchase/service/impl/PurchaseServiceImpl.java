package com.ryazancev.purchase.service.impl;

import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.DtoProcessor;
import com.ryazancev.purchase.util.MessageProcessor;
import com.ryazancev.purchase.util.exception.custom.PurchaseNotFoundException;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import com.ryazancev.purchase.util.validator.PurchaseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.ryazancev.purchase.util.exception.Message.PURCHASE_NOT_FOUND;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseValidator purchaseValidator;

    private final MessageProcessor messageProcessor;
    private final DtoProcessor dtoProcessor;

    private final ClientsService clientsService;


    @Override
    public PurchaseDto getById(String id) {

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() ->
                        new PurchaseNotFoundException(
                                PURCHASE_NOT_FOUND,
                                HttpStatus.NOT_FOUND));

        return dtoProcessor.createPurchaseDto(purchase);
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

        messageProcessor.updateCustomerBalance(customerId,
                availableCustomerBalance - selectedProductPrice);
        messageProcessor.updateProductQuantity(productId,
                availableProductsInStock - 1);
        messageProcessor.updateCustomerBalance(productOwnerId,
                ownerBalance + selectedProductPrice);

        Purchase saved = purchaseRepository.save(toSave);

        return dtoProcessor.createPurchaseDto(saved);
    }


    @Override
    public CustomerPurchasesResponse getByCustomerId(Long customerId) {

        List<Purchase> purchases = purchaseRepository
                .findByCustomerId(customerId);

        return dtoProcessor.createCustomerPurchasesResponse(purchases);
    }

}
