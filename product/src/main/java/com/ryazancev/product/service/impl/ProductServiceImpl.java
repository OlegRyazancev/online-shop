package com.ryazancev.product.service.impl;

import com.ryazancev.dto.admin.ObjectType;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.product.kafka.ProductProducerService;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.DeletedProductException;
import com.ryazancev.product.util.exception.custom.OrganizationNotFoundException;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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
        if (statusCheck) {

            return findByIdWithStatusCheck(id);
        }else {
            return findById(id);
        }
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
                                "Organization not found",
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
    public void changeStatusAndRegister(Long id,
                                        ProductStatus status) {
        Product existing = findByIdWithStatusCheck(id);

        existing.setStatus(status);
        existing.setRegisteredAt(LocalDateTime.now());

        //todo: send email in case of status of product
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
                    )
            }
    )
    public Product update(Product product) {

        Product existing = findByIdWithStatusCheck(product.getId());

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

        Product existing = findByIdWithStatusCheck(productId);
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
    public void markProductAsDeleted(Long id) {

        Product product = findByIdWithStatusCheck(id);

        product.setDescription("DELETED");
        product.setPrice(0.0);
        product.setKeywords("DELETED");
        product.setQuantityInStock(0);
        product.setStatus(ProductStatus.DELETED);

        productProducerService.sendMessageToReviewTopic(id);
        productRepository.save(product);
    }

    private Product findByIdWithStatusCheck(Long productId) {

        Product found = findById(productId);

        if (found.getStatus().equals(ProductStatus.DELETED)) {
            throw new DeletedProductException(
                    "Can not get deleted product",
                    HttpStatus.BAD_REQUEST);
        }
        //todo: check if product is frozen
        return found;
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

        productProducerService.sendMessageToAdminTopic(requestDTO);
    }
}
