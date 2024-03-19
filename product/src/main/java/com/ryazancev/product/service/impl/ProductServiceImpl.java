package com.ryazancev.product.service.impl;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;
import com.ryazancev.product.util.processor.KafkaMessageProcessor;
import com.ryazancev.product.util.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    private final KafkaMessageProcessor kafkaMessageProcessor;

    private final MessageSource messageSource;

    @Override
    @Cacheable(value = "Product::getAll")
    public List<Product> getAll() {

        return productRepository.findAll();
    }

    @Override
    @Cacheable(
            value = "Product::getById",
            key = "#id"
    )
    public Product getById(final Long id,
                           final boolean statusCheck) {

        Product existing = findById(id);

        if (statusCheck) {
            productValidator.validateStatus(existing, ProductStatus.INACTIVE);
            productValidator.validateStatus(existing, ProductStatus.DELETED);
        }
        return existing;
    }

    @Override
    @Cacheable(
            value = "Product::getByOrganizationId",
            key = "#organizationId"
    )
    public List<Product> getByOrganizationId(final Long organizationId) {

        return productRepository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new ProductNotFoundException(
                        messageSource.getMessage(
                                "exception.product.not_found_by_org_id",
                                new Object[]{organizationId},
                                Locale.getDefault()
                        ),
                        HttpStatus.NOT_FOUND)
                );
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            value = "Product::getAll",
                            allEntries = true
                    ),
                    @CacheEvict(
                            value = "Product::getByOrganizationId",
                            key = "#product.organizationId"
                    )}
    )
    public Product makeRegistrationRequest(final Product product) {

        productValidator.validateNameUniqueness(product);

        product.setStatus(ProductStatus.INACTIVE);

        Product saved = productRepository.save(product);

        kafkaMessageProcessor
                .sendRegistrationRequestToAdminTopic(saved.getId());
        kafkaMessageProcessor.sendNewRegistrationRequestNotification();


        return saved;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(
                    value = "Product::getById",
                    key = "#id"
            ),
            @CacheEvict(
                    value = "Product::getAll",
                    allEntries = true
            )}
    )
    public void changeStatus(final Long id,
                             final ProductStatus status) {

        Product existing = findById(id);

        existing.setStatus(status);

        productRepository.save(existing);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(
                    value = "Product::getById",
                    key = "#id"
            ),
            @CacheEvict(
                    value = "Product::getAll",
                    allEntries = true
            )}
    )
    public void register(final Long id) {

        Product existing = findById(id);

        existing.setRegisteredAt(LocalDateTime.now());

        productRepository.save(existing);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            value = "Product::getAll",
                            allEntries = true
                    ),
                    @CacheEvict(
                            value = "Product::getByOrganizationId",
                            key = "#product.organizationId"
                    )
            },
            put = {
                    @CachePut(
                            value = "Product::getById",
                            key = "#product.id"
                    )}
    )
    public Product update(final Product product) {

        Product existing = findById(product.getId());

        productValidator.validateAllStatus(existing);

        existing.setProductName(product.getProductName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setQuantityInStock(product.getQuantityInStock());
        existing.setKeywords(product.getKeywords());

        return productRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "Product::getById",
            key = "#productId"
    )
    public void updateQuantity(final Long productId,
                               final Integer quantityInStock) {

        Product existing = findById(productId);
        existing.setQuantityInStock(quantityInStock);

        productRepository.save(existing);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(
                    value = "Product::getAll",
                    allEntries = true),
            @CacheEvict(
                    value = "Product::getByOrganizationId",
                    allEntries = true
            ),
            @CacheEvict(
                    value = "Product::getById",
                    key = "#id"
            )
    })
    public String markProductAsDeleted(final Long id) {

        Product existing = findById(id);

        productValidator.validateAllStatus(existing);

        existing.setDescription("DELETED");
        existing.setPrice(0.0);
        existing.setKeywords("DELETED");
        existing.setQuantityInStock(0);
        existing.setStatus(ProductStatus.DELETED);

        kafkaMessageProcessor.sendProductIdToDeleteReviewTopic(id);

        productRepository.save(existing);

        return messageSource.getMessage(
                "service.product.deleted",
                null,
                Locale.getDefault()
        );
    }

    private Product findById(final Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        messageSource.getMessage(
                                "exception.product.not_found",
                                new Object[]{productId},
                                Locale.getDefault()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }
}
