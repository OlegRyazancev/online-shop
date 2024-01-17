package com.ryazancev.purchase.service.impl;

import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.repository.PurchaseRepository;
import com.ryazancev.purchase.service.PurchaseService;
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

    @Transactional
    @Override
    public Purchase processPurchase(Purchase purchase) {
        purchase.setPurchaseDate(LocalDateTime.now());
        log.info("Saving purchase: {}", purchase);
        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> getByCustomerId(Long customerId) {
        return purchaseRepository.findByCustomerId(customerId);
    }
}
