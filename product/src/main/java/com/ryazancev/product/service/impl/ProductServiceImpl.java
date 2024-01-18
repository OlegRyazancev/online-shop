package com.ryazancev.product.service.impl;

import com.ryazancev.clients.organization.OrganizationDTO;
import com.ryazancev.clients.product.ProductInfoDTO;
import com.ryazancev.clients.product.ProductsGetResponse;
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
    public ProductsGetResponse getAvailableProducts() {
        List<Product> products = productRepository.findAll();
        log.info(String.valueOf(products.size()));
        return ProductsGetResponse.builder()
                .products(productMapper.toDTO(products))
                .build();

    }

    @Override
    public ProductInfoDTO getById(Long productId) {
        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));

        ProductInfoDTO productInfoDTO = productMapper.toDTO(foundProduct);

        OrganizationDTO organization = OrganizationDTO.builder()
                .id(1L)
                .name("IKEA")
                .build();
        //todo:OrganizationClient
        productInfoDTO.setOrganization(organization);

        //todo: get reviews (Review client)
        //todo: count average rating (Review client)
        log.info(productInfoDTO.toString());
        return productInfoDTO;
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
