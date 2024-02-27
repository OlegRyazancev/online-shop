package com.ryazancev.product.service.impl;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.product.kafka.ProductProducerService;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.OrganizationNotFoundException;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;
import com.ryazancev.product.util.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.ryazancev.product.util.exception.Message.ORGANIZATION_NOT_FOUND;
import static com.ryazancev.product.util.exception.Message.PRODUCT_NOT_FOUND;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    private final ProductProducerService productProducerService;

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
    public Product getById(Long id, boolean statusCheck) {
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
    public List<Product> getByOrganizationId(Long organizationId) {

        return productRepository.findByOrganizationId(organizationId)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                ORGANIZATION_NOT_FOUND,
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
    public Product makeRegistrationRequest(Product product) {

        productValidator.validateNameUniqueness(product);

        product.setStatus(ProductStatus.INACTIVE);

        Product saved = productRepository.save(product);

        sendRegistrationRequestToAdmin(saved.getId());

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
    public void changeStatus(Long id,
                             ProductStatus status) {

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
    public void register(Long id) {

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
    public Product update(Product product) {

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
    public void updateQuantity(Long productId, Integer quantityInStock) {

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
    public String markProductAsDeleted(Long id) {

        Product existing = findById(id);

        productValidator.validateAllStatus(existing);

        existing.setDescription("DELETED");
        existing.setPrice(0.0);
        existing.setKeywords("DELETED");
        existing.setQuantityInStock(0);
        existing.setStatus(ProductStatus.DELETED);

        productProducerService.sendMessageToReviewTopic(id);
        productRepository.save(existing);

        return "Product was successfully deleted";
    }

    private Product findById(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        PRODUCT_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));
    }

    private void sendRegistrationRequestToAdmin(Long productId) {

        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .objectToRegisterId(productId)
                .objectType(ObjectType.PRODUCT)
                .build();

        productProducerService.sendMessageToAdminTopic(requestDto);
    }
}
