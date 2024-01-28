package com.ryazancev.product.controller;

import com.ryazancev.clients.product.dto.ProductDTO;
import com.ryazancev.clients.product.dto.ProductEditDTO;
import com.ryazancev.clients.product.dto.ProductsSimpleListResponse;
import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsProductResponse;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
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
    public ProductsSimpleListResponse getAll() {

        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductDTO getSimpleById(
            @PathVariable("id") Long id) {

        return productService.getSimpleById(id);
    }

    @GetMapping("/{id}/details")
    public ProductDTO getDetailedById(
            @PathVariable("id") Long id) {

        return productService.getDetailedById(id);
    }

    @GetMapping("/organizations/{id}")
    public ProductsSimpleListResponse getProductsByOrganizationId(
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
    public ProductDTO createProduct(
            @RequestBody
            @Validated(OnCreate.class)
            ProductEditDTO productEditDTO) {

        return productService.create(productEditDTO);
    }

    @PutMapping("/{id}/update-quantity")
    public ProductDTO updateQuantity(
            @PathVariable("id") Long productId,
            @RequestParam("quantity") Integer quantity) {

        return productService.updateQuantity(productId, quantity);
    }

    @PutMapping
    public ProductDTO updateProduct(
            @RequestBody
            @Validated(OnUpdate.class)
            ProductEditDTO productEditDTO) {

        return productService.update(productEditDTO);
    }

    //todo: delete product
}
