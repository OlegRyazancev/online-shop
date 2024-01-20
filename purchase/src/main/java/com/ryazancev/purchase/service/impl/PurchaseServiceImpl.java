package com.ryazancev.purchase.service.impl;

import com.ryazancev.clients.customer.CustomerClient;
import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.clients.product.ProductClient;
import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.purchase.PurchaseDetailedDTO;
import com.ryazancev.clients.purchase.PurchasePostDTO;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.mappers.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    private final ProductClient productClient;
    private final CustomerClient customerClient;

    @Transactional
    @Override
    public PurchaseDetailedDTO processPurchase(PurchasePostDTO purchasePostDTO) {

        ProductDetailedDTO selectedProduct = productClient
                .getInfoById(purchasePostDTO.getProductId());
        CustomerDTO selectedCustomer = customerClient
                .getInfoById(purchasePostDTO.getCustomerId());

        Double selectedProductPrice = selectedProduct.getPrice();
        Integer availableProductsInStock = selectedProduct.getQuantityInStock();
        Double availableCustomerBalance = selectedCustomer.getBalance();

        //todo: replace by custom exceptions
        if (availableCustomerBalance < selectedProductPrice) {
            throw new IllegalArgumentException("Customer doesn't have enough money to purchase the product");
        }
        if (availableProductsInStock == 0) {
            throw new IllegalArgumentException("No available products in stock");
        }

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

        return purchaseMapper.toDetailedDTO(purchaseRepository.save(purchaseToSave));
    }

    @Override
    public CustomerPurchasesResponse getByCustomerId(Long customerId) {
        List<Purchase> purchases = purchaseRepository.findByCustomerId(customerId);
        return CustomerPurchasesResponse.builder()
                .purchases(purchaseMapper.toDetailedDTO(purchases))
                .build();
    }
}
