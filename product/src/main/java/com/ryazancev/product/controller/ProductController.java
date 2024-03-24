package com.ryazancev.product.controller;

import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.product.ProductEditDto;
import com.ryazancev.common.dto.product.ProductsSimpleResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import com.ryazancev.common.validation.OnCreate;
import com.ryazancev.common.validation.OnUpdate;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.service.CustomExpressionService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.RequestHeader;
import com.ryazancev.product.util.mapper.ProductMapper;
import com.ryazancev.product.util.processor.DtoProcessor;
import com.ryazancev.product.util.processor.KafkaMessageProcessor;
import com.ryazancev.product.util.validator.ProductValidator;
import jakarta.servlet.http.HttpServletRequest;
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
    private final KafkaMessageProcessor kafkaMessageProcessor;

    private final DtoProcessor dtoProcessor;
    private final CustomExpressionService customExpressionService;
    private final HttpServletRequest httpServletRequest;

    @GetMapping
    public ProductsSimpleResponse getAll() {

        customExpressionService.checkIfAccountLocked();

        List<Product> products = productService.getAll();

        return dtoProcessor.createProductsSimpleResponse(products);
    }

    @GetMapping("/{id}")
    public ProductDto getById(
            @PathVariable("id") final Long id) {

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
            final ProductEditDto productEditDto) {

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
            final ProductEditDto productEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessProduct(productEditDto.getId());

        Product product = productMapper.toEntity(productEditDto);
        Product updated = productService.update(product);

        return dtoProcessor
                .createProductDetailedDtoWithOrganizationAndAvgRating(updated);
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(
            @PathVariable("id") final Long id) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessProduct(id);

        return productService.markProductAsDeleted(id);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByProductId(
            @PathVariable("id") final Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = false;

        Product product = productService.getById(id, statusCheck);

        return dtoProcessor.getReviewsResponseByProductId(product.getId());
    }

    @PostMapping("/reviews")
    public ReviewDto createReview(
            @RequestBody
            @Validated(OnCreate.class)
            final ReviewEditDto reviewEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessPurchase(
                reviewEditDto.getPurchaseId());

        ReviewDto created = dtoProcessor.createReviewDto(reviewEditDto);
        Long productId = created
                .getPurchase().safelyCast(PurchaseDto.class, false)
                .getProduct().safelyCast(ProductDto.class, false)
                .getId();

        Long organizationId =
                productService
                        .getById(productId, false)
                        .getOrganizationId();

        kafkaMessageProcessor
                .sendReviewCreatedNotification(
                        created,
                        organizationId,
                        new RequestHeader(httpServletRequest)
                );

        return created;
    }

    //    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    public ProductDto getSimpleById(
            @PathVariable("id") final Long id) {

        boolean statusCheck = false;

        Product product = productService.getById(id, statusCheck);

        return dtoProcessor.createProductSimpleDto(product);
    }

    @GetMapping("/{id}/price-quantity")
    public PriceQuantityResponse getPriceAndQuantityByProductId(
            @PathVariable("id") final Long productId) {

        boolean statusCheck = true;

        Product product = productService.getById(productId, statusCheck);

        productValidator.validateStatus(product, ProductStatus.FROZEN);

        return dtoProcessor.createPriceQuantityResponse(product);
    }

    @GetMapping("/organizations/{id}")
    public ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("id") final Long id) {

        List<Product> organizationProducts =
                productService.getByOrganizationId(id);

        return dtoProcessor.createProductsSimpleResponse(organizationProducts);
    }

    @GetMapping("/{id}/owner-id")
    public Long getOwnerId(
            @PathVariable("id") final Long productId) {

        boolean statusCheck = false;

        Product product = productService.getById(productId, statusCheck);

        return dtoProcessor
                .getOrganizationOwnerId(product.getOrganizationId());
    }
}
