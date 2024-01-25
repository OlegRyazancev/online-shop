package com.ryazancev.product.service.impl;

import com.ryazancev.clients.organization.OrganizationClient;
import com.ryazancev.clients.organization.OrganizationDTO;
import com.ryazancev.clients.product.*;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;
import com.ryazancev.product.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final OrganizationClient organizationClient;

    @Override
    public ProductListResponse getAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productsDTO = productMapper.toListDTO(products);
        return ProductListResponse.builder()
                .products(productsDTO)
                .build();
    }

    @Override
    public ProductDTO getById(Long productId) {
        Product existing = findById(productId);

        return productMapper.toSimpleDTO(existing);
    }

    @Override
    public ProductDetailedDTO getDetailedById(Long productId) {
        Product existing = findById(productId);

        ProductDetailedDTO productDetailedDTO = productMapper.toDetailedDTO(existing);

        OrganizationDTO productOrganization = organizationClient.getById(existing.getOrganizationId());
        productDetailedDTO.setOrganization(productOrganization);
        //todo: get reviews (Review client)
        //todo: count average rating (Review client)
        return productDetailedDTO;
    }

    @Override
    public ProductListResponse getByOrganizationId(Long organizationId) {
        List<Product> products = productRepository.findByOrganizationId(organizationId);
        return ProductListResponse.builder()
                .products(productMapper.toListDTO(products))
                .build();

    }

    @Transactional
    @Override
    public ProductDetailedDTO create(ProductCreateDTO productCreateDTO) {

        if (productRepository.findByProductName(productCreateDTO.getProductName()).isPresent()) {
            throw new ProductCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        Product productToSave = productMapper.toEntity(productCreateDTO);
        Product savedProduct = productRepository.save(productToSave);
        ProductDetailedDTO savedProductDetailedDTO = productMapper.toDetailedDTO(savedProduct);

        OrganizationDTO productOrganization = organizationClient.getById(savedProduct.getOrganizationId());
        savedProductDetailedDTO.setOrganization(productOrganization);
        return savedProductDetailedDTO;
    }

    @Transactional
    @Override
    public ProductDetailedDTO updateQuantity(Long productId, Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can not be less than 0");
        }
        Product existing = findById(productId);
        existing.setQuantityInStock(quantity);
        return productMapper.toDetailedDTO(existing);

    }

    @Transactional
    @Override
    public ProductDetailedDTO update(ProductUpdateDTO productUpdateDTO) {

        Product existing = findById(productUpdateDTO.getId());

        existing.setProductName(productUpdateDTO.getProductName());
        existing.setDescription(productUpdateDTO.getDescription());
        existing.setPrice(productUpdateDTO.getPrice());
        existing.setQuantityInStock(productUpdateDTO.getQuantityInStock());
        existing.setKeywords(String.join(", ", productUpdateDTO.getKeywords()));

        Product savedProduct = productRepository.save(existing);

        ProductDetailedDTO savedProductDetailedDTO = productMapper.toDetailedDTO(savedProduct);

        OrganizationDTO productOrganization = organizationClient.getById(existing.getOrganizationId());
        savedProductDetailedDTO.setOrganization(productOrganization);

        return savedProductDetailedDTO;
    }

    private Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found",
                        HttpStatus.NOT_FOUND
                ));
    }
}
