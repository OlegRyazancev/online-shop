package com.ryazancev.product.controller;

import com.ryazancev.dto.product.PriceQuantityResponse;
import com.ryazancev.dto.product.ProductDto;
import com.ryazancev.dto.product.ProductEditDto;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.dto.review.ReviewDto;
import com.ryazancev.dto.review.ReviewEditDto;
import com.ryazancev.dto.review.ReviewsResponse;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.CustomExpressionService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.ProductUtil;
import com.ryazancev.product.util.mapper.ProductMapper;
import com.ryazancev.product.util.validator.ProductValidator;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;
    private final ProductUtil productUtil;

    private final ClientsService clientsService;

    private final CustomExpressionService customExpressionService;


    @GetMapping
    public ProductsSimpleResponse getAll() {

        customExpressionService.checkIfAccountLocked();

        List<Product> products = productService.getAll();

        return ProductsSimpleResponse.builder()
                .products(productMapper.toSimpleListDto(products))
                .build();
    }

    @GetMapping("/{id}")
    public ProductDto getById(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();

        boolean statusCheck = true;

        Product product = productService.getById(id, statusCheck);
        return productUtil.createDetailedProductDto(product);
    }


    @PostMapping
    public ProductDto makeRegistrationRequestOfProduct(
            @RequestBody
            @Validated(OnCreate.class)
            ProductEditDto productEditDto) {

        customExpressionService.checkAccountPermissions();
        customExpressionService.checkAccessOrganization(productEditDto);

        Product product = productMapper.toEntity(productEditDto);
        Product saved = productService.makeRegistrationRequest(product);
        ProductDto productDto = productMapper.toDetailedDto(saved);

        productUtil.setOrganizationDto(productDto, product.getOrganizationId());

        return productDto;
    }


    @PutMapping
    public ProductDto updateProduct(
            @RequestBody
            @Validated(OnUpdate.class)
            ProductEditDto productEditDto) {

        customExpressionService.checkAccountPermissions();
        customExpressionService.checkAccessProduct(productEditDto.getId());

        Product product = productMapper.toEntity(productEditDto);
        Product updated = productService.update(product);
        return productUtil.createDetailedProductDto(updated);
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable("id") Long id) {

        customExpressionService.checkAccountPermissions();
        customExpressionService.checkAccessProduct(id);

        return productService.markProductAsDeleted(id);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByProductId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();

        return (ReviewsResponse) clientsService.getReviewsByProductId(id);
    }

    @PostMapping("/reviews")
    public ReviewDto createReview(
            @RequestBody
            @Validated({OnCreate.class})
            ReviewEditDto reviewEditDto) {

        customExpressionService.checkAccountPermissions();
        customExpressionService.checkAccessPurchase(
                reviewEditDto.getPurchaseId());

        return (ReviewDto) clientsService.createReview(reviewEditDto);
    }

    //    Endpoints only  for feign clients


    @GetMapping("/{id}/simple")
    public ProductDto getSimpleById(
            @PathVariable("id") Long id) {

        boolean statusCheck = false;

        Product product = productService.getById(id, statusCheck);

        return productMapper.toSimpleDto(product);
    }

    @GetMapping("/{id}/price-quantity")
    public PriceQuantityResponse getPriceAndQuantityByProductId(
            @PathVariable("id") Long productId) {

        boolean statusCheck = true;

        Product product = productService.getById(productId, statusCheck);

        productValidator.validateStatus(product, ProductStatus.FROZEN);

        return PriceQuantityResponse.builder()
                .price(product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .build();

    }

    @GetMapping("/organizations/{id}")
    public ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("id") Long id) {

        List<Product> organizationProducts =
                productService.getByOrganizationId(id);

        return ProductsSimpleResponse.builder()
                .products(productMapper.toSimpleListDto(
                        organizationProducts))
                .build();
    }

    @GetMapping("/{id}/owner-id")
    public Long getOwnerId(
            @PathVariable("id") Long productId) {

        boolean statusCheck = false;

        Product product = productService.getById(productId, statusCheck);

        return (Long) clientsService
                .getOrganizationOwnerIdById(product.getOrganizationId());
    }
}
