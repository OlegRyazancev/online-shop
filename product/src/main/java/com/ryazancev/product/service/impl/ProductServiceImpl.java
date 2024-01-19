package com.ryazancev.product.service.impl;

import com.ryazancev.clients.organization.OrganizationDTO;
import com.ryazancev.clients.product.ProductDTO;
import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductPostDTO;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.product.util.mappers.ProductMapper;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
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

    @Override
    public ProductListResponse getAvailableProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productsDTO = productMapper.toDTO(products);
        return ProductListResponse.builder()
                .products(productsDTO)
                .build();
    }

    @Transactional
    @Override
    public ProductDetailedDTO save(ProductPostDTO productPostDTO) {

        //todo: add checks name, etc
        Product productToSave = productMapper.toEntity(productPostDTO);
        Product savedProduct = productRepository.save(productToSave);
        ProductDetailedDTO savedDtoProduct = productMapper.toDTO(savedProduct);

        //todo:OrganizationClient
        OrganizationDTO organization = OrganizationDTO.builder()
                .id(1L)
                .name("IKEA")
                .build();

        savedDtoProduct.setOrganization(organization);
        log.info("saved product has id: {}", savedProduct.getId());
        return savedDtoProduct;
    }

    @Override
    public ProductDetailedDTO getById(Long productId) {
        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));

        ProductDetailedDTO productDetailedDTO = productMapper.toDTO(foundProduct);

        //todo:OrganizationClient
        OrganizationDTO organization = OrganizationDTO.builder()
                .id(1L)
                .name("IKEA")
                .build();

        productDetailedDTO.setOrganization(organization);

        //todo: get reviews (Review client)
        //todo: count average rating (Review client)
        log.info(productDetailedDTO.toString());
        return productDetailedDTO;
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
