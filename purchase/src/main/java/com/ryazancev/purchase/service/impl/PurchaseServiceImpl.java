package com.ryazancev.purchase.service.impl;

import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.PurchaseDTO;
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

    @Transactional
    @Override
    public PurchaseDTO processPurchase(PurchaseDTO purchaseDTO) {
        //todo: user's money (check available and make it less)
        //todo: product in stock
        Purchase purchaseToSave = purchaseMapper.toEntity(purchaseDTO);
        purchaseToSave.setPurchaseDate(LocalDateTime.now());
        log.info("Saving purchase: {}", purchaseToSave);
        Purchase processedPurchase = purchaseRepository.save(purchaseToSave);
        return purchaseMapper.toDTO(processedPurchase);
    }

    @Override
    public CustomerPurchasesResponse getByCustomerId(Long customerId) {
        List<Purchase> purchases = purchaseRepository.findByCustomerId(customerId);
        return CustomerPurchasesResponse.builder()
                .purchases(purchaseMapper.toDTO(purchases))
                .build();
    }
}
