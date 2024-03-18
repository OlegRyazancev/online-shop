package com.ryazancev.purchase.service.impl;

import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.exception.custom.PurchaseNotFoundException;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import com.ryazancev.purchase.util.processor.DtoProcessor;
import com.ryazancev.purchase.util.processor.KafkaMessageProcessor;
import com.ryazancev.purchase.util.validator.PurchaseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseValidator purchaseValidator;

    private final KafkaMessageProcessor kafkaMessageProcessor;
    private final DtoProcessor dtoProcessor;

    private final ClientsService clientsService;
    private final MessageSource messageSource;


    @Override
    public PurchaseDto getById(String id) {

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(
                        messageSource.getMessage(
                                "exception.purchase.not_found_by_id",
                                new Object[]{id},
                                Locale.getDefault()
                        ),
                        HttpStatus.NOT_FOUND));

        return dtoProcessor.createPurchaseDto(purchase);
    }

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

        kafkaMessageProcessor.updateCustomerBalance(customerId,
                availableCustomerBalance - selectedProductPrice);
        kafkaMessageProcessor.updateProductQuantity(productId,
                availableProductsInStock - 1);
        kafkaMessageProcessor.updateCustomerBalance(productOwnerId,
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
