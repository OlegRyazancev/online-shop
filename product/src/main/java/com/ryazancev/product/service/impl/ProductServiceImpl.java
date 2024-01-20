package com.ryazancev.product.service.impl;

import com.ryazancev.clients.organization.OrganizationClient;
import com.ryazancev.clients.organization.OrganizationDTO;
import com.ryazancev.clients.product.*;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.mappers.ProductMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        List<ProductDTO> productsDTO = productMapper.toDTO(products);
        return ProductListResponse.builder()
                .products(productsDTO)
                .build();
    }

    @Transactional
    @Override
    public ProductDetailedDTO create(ProductCreateDTO productCreateDTO) {

        //todo: add checks name, etc
        Product productToSave = productMapper.toEntity(productCreateDTO);
        Product savedProduct = productRepository.save(productToSave);
        ProductDetailedDTO savedProductDetailedDTO = productMapper.toDTO(savedProduct);

        OrganizationDTO productOrganization = organizationClient.getById(savedProduct.getOrganizationId());
        savedProductDetailedDTO.setOrganization(productOrganization);
        return savedProductDetailedDTO;
    }


    @Override
    public ProductDetailedDTO getById(Long productId) {
        Product existing = getProductById(productId);

        ProductDetailedDTO productDetailedDTO = productMapper.toDTO(existing);

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
                .products(productMapper.toDTO(products))
                .build();

    }

    @Transactional
    @Override
    public ProductDetailedDTO updateQuantity(Long productId, Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can not be less than 0");
        }
        Product existing = getProductById(productId);
        existing.setQuantityInStock(quantity);
        return productMapper.toDTO(existing);

    }

    @Override
    public Boolean isOrganizationProduct(Long productId, Long organizationId) {
        return productRepository.existsByIdAndOrganizationId(productId, organizationId);
    }

    @Transactional
    @Override
    public ProductDetailedDTO update(ProductUpdateDTO productUpdateDTO) {
        Product existing = getProductById(productUpdateDTO.getId());
        //todo: some checks??
        existing.setProductName(productUpdateDTO.getProductName());
        existing.setDescription(productUpdateDTO.getDescription());
        existing.setPrice(productUpdateDTO.getPrice());
        existing.setQuantityInStock(productUpdateDTO.getQuantityInStock());
        existing.setKeywords(String.join(", ", productUpdateDTO.getKeywords()));

        Product savedProduct = productRepository.save(existing);

        ProductDetailedDTO savedProductDetailedDTO = productMapper.toDTO(savedProduct);

        OrganizationDTO productOrganization = organizationClient.getById(existing.getOrganizationId());
        savedProductDetailedDTO.setOrganization(productOrganization);

        return savedProductDetailedDTO;
    }

    @Transactional
    @Override
    public void delete(Long productId) {

    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
