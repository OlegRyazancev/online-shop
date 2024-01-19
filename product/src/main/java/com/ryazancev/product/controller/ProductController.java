package com.ryazancev.product.controller;

import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.clients.product.ProductPostDTO;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ProductListResponse getAll() {
        return productService.getAll();
    }

    @GetMapping("/{productId}")
    public ProductDetailedDTO getProductInfo(@PathVariable("productId") Long productId) {
        return productService.getById(productId);
    }

    @GetMapping("/organizations/{organizationId}")
    public ProductListResponse getByOrganizationId(@PathVariable("organizationId") Long organizationId) {
        return productService.getByOrganizationId(organizationId);
    }

    @PostMapping
    public ProductDetailedDTO save(@RequestBody ProductPostDTO productPostDTO) {
        return productService.save(productPostDTO);
    }

    //todo: get Reviews and rating of a product (communicate to review service)
    //todo: set Review and rating of a product (communicate to review service)
}
