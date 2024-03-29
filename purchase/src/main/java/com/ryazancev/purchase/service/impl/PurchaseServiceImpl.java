package com.ryazancev.purchase.service.impl;

import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.exception.CustomExceptionFactory;
import com.ryazancev.purchase.util.mapper.PurchaseMapper;
import com.ryazancev.purchase.util.processor.DtoProcessor;
import com.ryazancev.purchase.util.processor.KafkaMessageProcessor;
import com.ryazancev.purchase.util.validator.PurchaseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    @Override
    public PurchaseDto getById(final String id) {

        Purchase purchase = findById(id);

        return dtoProcessor.createPurchaseDto(purchase);
    }

    @Override
    public PurchaseDto processPurchase(
            final PurchaseEditDto purchaseEditDto) {

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

        Purchase saved = purchaseRepository.save(toSave);

        kafkaMessageProcessor.updateCustomerBalance(customerId,
                availableCustomerBalance - selectedProductPrice);
        kafkaMessageProcessor.updateProductQuantity(productId,
                availableProductsInStock - 1);
        kafkaMessageProcessor.updateCustomerBalance(productOwnerId,
                ownerBalance + selectedProductPrice);

        return dtoProcessor.createPurchaseDto(saved);
    }


    @Override
    public CustomerPurchasesResponse getByCustomerId(
            final Long customerId) {

        List<Purchase> purchases = purchaseRepository
                .findByCustomerId(customerId);

        return dtoProcessor.createCustomerPurchasesResponse(purchases);
    }

    private Purchase findById(final String id) {

        return purchaseRepository.findById(id)
                .orElseThrow(() ->
                        CustomExceptionFactory
                                .getPurchaseNotFound()
                                .byId(id)
                );
    }
}
