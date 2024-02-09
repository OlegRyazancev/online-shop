package com.ryazancev.product.service.impl;

import com.ryazancev.dto.admin.ObjectType;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

//TODO: REFACTOR (make mapper to controller)
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    private final KafkaTemplate<String, RegistrationRequestDTO> kafkaTemplate;

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
    public Product getById(Long id) {

        return findById(id);
    }

    @Override
    @Cacheable(
            value = "Product::getByOrganizationId",
            key = "#organizationId"
    )
    public List<Product> getByOrganizationId(Long organizationId) {

        return productRepository
                .findByOrganizationId(organizationId);
    }

    @Transactional
    @Override
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

        if (productRepository.findByProductName(
                product.getProductName()).isPresent()) {
            throw new ProductCreationException(
                    "Product with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        product.setStatus(ProductStatus.INACTIVE);

        Product saved = productRepository.save(product);

        sendRegistrationRequestToAdmin(saved.getId());

        return saved;
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "Product::getById",
            key = "#id"
    )
    public void changeStatusAndRegister(Long id,
                                        ProductStatus status) {
        Product existing = findById(id);

        existing.setStatus(status);
        existing.setRegisteredAt(LocalDateTime.now());

        //todo: send email in case of status of product
        productRepository.save(existing);
    }


    @Transactional
    @Override
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
                    )
            }
    )
    public Product update(Product product) {

        Product existing = findById(product.getId());

        existing.setProductName(product.getProductName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setQuantityInStock(product.getQuantityInStock());
        existing.setKeywords(product.getKeywords());

        return productRepository.save(existing);
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "Product::getById",
            key = "#productId"
    )
    public void updateQuantity(Long productId, Integer quantityInStock) {

        Product existing = findById(productId);
        existing.setQuantityInStock(quantityInStock);

        productRepository.save(existing);
    }

    private Product findById(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found",
                        HttpStatus.NOT_FOUND
                ));
    }

    private void sendRegistrationRequestToAdmin(Long productId) {

        RegistrationRequestDTO requestDTO = RegistrationRequestDTO.builder()
                .objectToRegisterId(productId)
                .objectType(ObjectType.PRODUCT)
                .build();

        kafkaTemplate.send(adminTopic, requestDTO);
    }
}
