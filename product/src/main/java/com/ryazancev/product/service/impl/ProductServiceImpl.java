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
        ProductDetailedDTO savedDtoProduct = productMapper.toDTO(savedProduct);

        OrganizationDTO productOrganization = organizationClient.getById(savedProduct.getOrganizationId());
        savedDtoProduct.setOrganization(productOrganization);
        return savedDtoProduct;
    }


    @Override
    public ProductDetailedDTO getById(Long productId) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));

        OrganizationDTO productOrganization = organizationClient.getById(existing.getOrganizationId());

        ProductDetailedDTO productDetailedDTO = productMapper.toDTO(existing);
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
        Product existing = productRepository.findById(productId)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));
        existing.setQuantityInStock(quantity);
        return productMapper.toDTO(existing);

    }


    @Transactional
    @Override
    public void delete(Long productId) {

    }
}
