package com.ryazancev.product.controller;

import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.product.ProductEditDto;
import com.ryazancev.common.dto.product.ProductsSimpleResponse;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import com.ryazancev.common.validation.OnCreate;
import com.ryazancev.common.validation.OnUpdate;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.CustomExpressionService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.DtoProcessor;
import com.ryazancev.product.util.MailProcessor;
import com.ryazancev.product.util.mapper.ProductMapper;
import com.ryazancev.product.util.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    private final DtoProcessor dtoProcessor;
    private final CustomExpressionService customExpressionService;


    @GetMapping
    public ProductsSimpleResponse getAll() {

        customExpressionService.checkIfAccountLocked();

        List<Product> products = productService.getAll();

        return dtoProcessor.createProductsSimpleResponse(products);
    }

    @GetMapping("/{id}")
    public ProductDto getById(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = true;

        Product product = productService.getById(id, statusCheck);

        return dtoProcessor
                .createProductDetailedDtoWithOrganizationAndAvgRating(product);
    }


    @PostMapping
    public ProductDto makeRegistrationRequestOfProduct(
            @RequestBody
            @Validated(OnCreate.class)
            ProductEditDto productEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessOrganization(productEditDto);

        Product product = productMapper.toEntity(productEditDto);
        Product saved = productService.makeRegistrationRequest(product);

        return dtoProcessor.createProductDetailedDtoWithOrganization(saved);
    }

    @PutMapping
    public ProductDto updateProduct(
            @RequestBody
            @Validated(OnUpdate.class)
            ProductEditDto productEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessProduct(productEditDto.getId());

        Product product = productMapper.toEntity(productEditDto);
        Product updated = productService.update(product);

        return dtoProcessor
                .createProductDetailedDtoWithOrganizationAndAvgRating(updated);
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable("id") Long id) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessProduct(id);

        return productService.markProductAsDeleted(id);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByProductId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = false;

        Product product = productService.getById(id, statusCheck);

        return dtoProcessor.getReviewsResponseByProductId(product.getId());
    }

    @PostMapping("/reviews")
    public ReviewDto createReview(
            @RequestBody
            @Validated({OnCreate.class})
            ReviewEditDto reviewEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessPurchase(
                reviewEditDto.getPurchaseId());

        return dtoProcessor.createReviewDto(reviewEditDto);
    }

    //    Endpoints only  for feign clients


    @GetMapping("/{id}/simple")
    public ProductDto getSimpleById(
            @PathVariable("id") Long id) {

        boolean statusCheck = false;

        Product product = productService.getById(id, statusCheck);

        return dtoProcessor.createProductSimpleDto(product);
    }

    @GetMapping("/{id}/price-quantity")
    public PriceQuantityResponse getPriceAndQuantityByProductId(
            @PathVariable("id") Long productId) {

        boolean statusCheck = true;

        Product product = productService.getById(productId, statusCheck);

        productValidator.validateStatus(product, ProductStatus.FROZEN);

        return dtoProcessor.createPriceQuantityResponse(product);
    }

    @GetMapping("/organizations/{id}")
    public ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("id") Long id) {

        List<Product> organizationProducts =
                productService.getByOrganizationId(id);

        return dtoProcessor.createProductsSimpleResponse(organizationProducts);
    }

    @GetMapping("/{id}/owner-id")
    public Long getOwnerId(
            @PathVariable("id") Long productId) {

        boolean statusCheck = false;

        Product product = productService.getById(productId, statusCheck);

        return dtoProcessor
                .getOrganizationOwnerDtoById(product.getOrganizationId());
    }
}
