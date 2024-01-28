package com.ryazancev.purchase.service.impl;

import com.ryazancev.clients.customer.CustomerClient;
import com.ryazancev.clients.customer.dto.CustomerDetailedDTO;
import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.product.ProductClient;
import com.ryazancev.clients.product.dto.ProductDTO;
import com.ryazancev.clients.purchase.dto.PurchaseDetailedDTO;
import com.ryazancev.clients.purchase.dto.PurchasePostDTO;
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

    @Transactional
    @Override
    public PurchaseDetailedDTO processPurchase(
            PurchasePostDTO purchasePostDTO) {

        ProductDTO selectedProduct = productClient
                .getDetailedById(purchasePostDTO.getProductId());
        CustomerDetailedDTO selectedCustomer = customerClient
                .getDetailedById(purchasePostDTO.getCustomerId());

        Double selectedProductPrice = selectedProduct.getPrice();
        Integer availableProductsInStock = selectedProduct.getQuantityInStock();
        Double availableCustomerBalance = selectedCustomer.getBalance();

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
        //todo: make async
        customerClient.updateBalance(
                selectedCustomer.getId(),
                availableCustomerBalance - selectedProductPrice);
        productClient.updateQuantity(
                selectedProduct.getId(),
                availableProductsInStock - 1);

        Purchase purchaseToSave = purchaseMapper.toEntity(purchasePostDTO);
        purchaseToSave.setPurchaseDate(LocalDateTime.now());
        purchaseToSave.setAmount(selectedProductPrice);

        return purchaseMapper.toDetailedDTO(
                purchaseRepository.save(purchaseToSave));
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
        return CustomerPurchasesResponse.builder()
                .purchases(purchaseMapper.toDetailedListDTO(purchases))
                .build();
    }
}
