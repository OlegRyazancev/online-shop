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

    @GetMapping("/{id}")
    public ProductSimpleDTO getSimpleProductById(
            @PathVariable("id") Long id) {

        return productService.getSimpleById(id);
    }

    @GetMapping("/{id}/details")
    public ProductDetailedDTO getDetailedProductById(
            @PathVariable("id") Long id) {

        return productService.getDetailedById(id);
    }

    @GetMapping("/organizations/{id}")
    public ProductListResponse getProductsByOrganizationId(
            @PathVariable("id") Long id) {

        return productService.getByOrganizationId(id);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsProductResponse getReviewsByProductId(
            @PathVariable("id") Long id) {

        return productService.getReviewsByProductId(id);
    }

    @PostMapping("/reviews")
    public ReviewDetailedDTO createReview(
            @RequestBody ReviewPostDTO reviewPostDTO) {

        return productService.createReview(reviewPostDTO);
    }

    @PostMapping
    public ProductDetailedDTO createProduct(
            @RequestBody ProductCreateDTO productCreateDTO) {

        return productService.create(productCreateDTO);
    }

    @PutMapping("/{id}/update-quantity")
    public ProductDetailedDTO updateQuantity(
            @PathVariable("id") Long productId,
            @RequestParam("quantity") Integer quantity) {

        return productService.updateQuantity(productId, quantity);
    }

    @PutMapping
    public ProductDetailedDTO updateProduct(
            @RequestBody ProductUpdateDTO productUpdateDTO) {

        return productService.update(productUpdateDTO);
    }

    //todo: delete product
}
