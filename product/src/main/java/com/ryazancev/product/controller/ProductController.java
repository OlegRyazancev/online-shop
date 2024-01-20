package com.ryazancev.product.controller;

import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.clients.product.ProductCreateDTO;
import com.ryazancev.clients.product.ProductUpdateDTO;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ProductDetailedDTO getInfoById(@PathVariable("productId") Long productId) {
        return productService.getById(productId);
    }

    @GetMapping("/organizations/{organizationId}")
    public ProductListResponse getByOrganizationId(@PathVariable("organizationId") Long organizationId) {
        return productService.getByOrganizationId(organizationId);
    }

    @PostMapping
    public ProductDetailedDTO create(@RequestBody ProductCreateDTO productCreateDTO) {
        return productService.create(productCreateDTO);
    }

    @PutMapping("/{productId}/update-quantity")
    public ProductDetailedDTO updateQuantity(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity) {
        return productService.updateQuantity(productId, quantity);
    }

    @GetMapping("/{productId}/check-organization")
    public Boolean isOrganizationProduct(@PathVariable("productId") Long productId, @RequestParam("organizationId") Long organizationId) {
        return productService.isOrganizationProduct(productId, organizationId);
    }

    @PutMapping
    public ProductDetailedDTO update(@RequestBody ProductUpdateDTO productUpdateDTO) {
        log.info("Product update id: {}", productUpdateDTO.getId());

        return productService.update(productUpdateDTO);
    }

    //todo: get Reviews and rating of a product (communicate to review service)
    //todo: set Review and rating of a product (communicate to review service)
}