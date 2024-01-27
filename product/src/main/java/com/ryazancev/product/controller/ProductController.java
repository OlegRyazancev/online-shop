package com.ryazancev.product.controller;

import com.ryazancev.clients.product.*;
import com.ryazancev.clients.review.ReviewDetailedDTO;
import com.ryazancev.clients.review.ReviewPostDTO;
import com.ryazancev.clients.review.ReviewsProductResponse;
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
    public ProductDTO getById(@PathVariable("productId") Long productId) {
        return productService.getById(productId);
    }

    @GetMapping("/{productId}/details")
    public ProductDetailedDTO getDetailedById(@PathVariable("productId") Long productId) {
        return productService.getDetailedById(productId);
    }

    @GetMapping("/organizations/{organizationId}")
    public ProductListResponse getByOrganizationId(@PathVariable("organizationId") Long organizationId) {
        return productService.getByOrganizationId(organizationId);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsProductResponse getReviewsByProductId(@PathVariable("id") Long id) {
        return productService.getReviewsByProductId(id);
    }

    @PostMapping("/reviews")
    public ReviewDetailedDTO createReview(@RequestBody ReviewPostDTO reviewPostDTO) {
        return productService.createReview(reviewPostDTO);
    }

    @PostMapping
    public ProductDetailedDTO create(@RequestBody ProductCreateDTO productCreateDTO) {
        return productService.create(productCreateDTO);
    }

    @PutMapping("/{productId}/update-quantity")
    public ProductDetailedDTO updateQuantity(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity) {
        return productService.updateQuantity(productId, quantity);
    }

    @PutMapping
    public ProductDetailedDTO update(@RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.update(productUpdateDTO);
    }

    //todo: delete product
}
