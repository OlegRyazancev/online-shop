package com.ryazancev.product.controller;

import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductEditDTO;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.dto.review.ReviewsResponse;
import com.ryazancev.product.service.CustomExpressionService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
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
    private final CustomExpressionService customExpressionService;

    @GetMapping
    public ProductsSimpleResponse getAll() {

        return productService.getAll();
    }

    @GetMapping("/{id}/simple")
    public ProductDTO getSimpleById(
            @PathVariable("id") Long id) {

        return productService.getSimpleById(id);
    }

    @GetMapping("/{id}")
    public ProductDTO getById(
            @PathVariable("id") Long id) {

        return productService.getDetailedById(id);
    }

    @GetMapping("/organizations/{id}")
    public ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("id") Long id) {

        return productService.getByOrganizationId(id);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByProductId(
            @PathVariable("id") Long id) {

        return productService.getReviewsByProductId(id);
    }

    @PostMapping("/reviews")
    public ReviewDTO createReview(
            @RequestBody
            @Validated({OnCreate.class})
            ReviewPostDTO reviewPostDTO) {

        return productService.createReview(reviewPostDTO);
    }

    @PostMapping
    public ProductDTO makeRegistrationRequestOfProduct(
            @RequestBody
            @Validated(OnCreate.class)
            ProductEditDTO productEditDTO) {

        if (!customExpressionService
                .canAccessOrganization(productEditDTO.getOrganizationId())){

            throw new AccessDeniedException();
        }
        return productService.makeRegistrationRequest(productEditDTO);
    }

    @PutMapping
    public ProductDTO updateProduct(
            @RequestBody
            @Validated(OnUpdate.class)
            ProductEditDTO productEditDTO) {

        if (!customExpressionService
                .canAccessProduct(
                        productEditDTO.getId(),
                        productEditDTO.getOrganizationId())) {

            throw new AccessDeniedException();
        }

        return productService.update(productEditDTO);
    }

    //todo: delete product
}
