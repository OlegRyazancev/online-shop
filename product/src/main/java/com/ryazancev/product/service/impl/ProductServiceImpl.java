package com.ryazancev.product.service.impl;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getById(Long productId) {
        return null;
    }

    @Transactional
    @Override
    public Product create(Product product) {
        return null;
    }

    @Transactional
    @Override
    public Product update(Product product) {
        return null;
    }

    @Transactional
    @Override
    public void delete(Long productId) {

    }
}
