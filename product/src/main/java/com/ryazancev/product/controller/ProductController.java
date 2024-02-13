package com.ryazancev.product.controller;

import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.product.PriceQuantityResponse;
import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductEditDTO;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.dto.review.ReviewsResponse;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.service.expression.CustomExpressionService;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import com.ryazancev.product.util.mapper.ProductMapper;
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

    private final CustomExpressionService customExpressionService;

    private final OrganizationClient organizationClient;
    private final ReviewClient reviewClient;

    @GetMapping
    public ProductsSimpleResponse getAll() {

        List<Product> products = productService.getAll();

        return ProductsSimpleResponse.builder()
                .products(productMapper.toSimpleListDTO(products))
                .build();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(
            @PathVariable("id") Long id) {

        boolean statusCheck = true;

        Product product = productService.getById(id, statusCheck);
        ProductDTO productDTO = productMapper.toDetailedDTO(product);

        OrganizationDTO organizationDTO = organizationClient.getSimpleById(
                product.getOrganizationId());
        productDTO.setOrganization(organizationDTO);

        Double avgRating = reviewClient.getAverageRatingByProductId(id);
        productDTO.setAverageRating(avgRating);

        return productDTO;
    }


    @PostMapping
    public ProductDTO makeRegistrationRequestOfProduct(
            @RequestBody
            @Validated(OnCreate.class)
            ProductEditDTO productEditDTO) {

        if (!customExpressionService
                .canAccessOrganization(
                        productEditDTO.getOrganizationId())) {

            throw new AccessDeniedException();
        }

        Product product = productMapper.toEntity(productEditDTO);
        Product saved = productService.makeRegistrationRequest(product);
        ProductDTO productDTO = productMapper.toDetailedDTO(saved);


        OrganizationDTO organizationDTO =
                organizationClient.getSimpleById(
                        product.getOrganizationId());
        productDTO.setOrganization(organizationDTO);

        return productDTO;
    }

    @PutMapping
    public ProductDTO updateProduct(
            @RequestBody
            @Validated(OnUpdate.class)
            ProductEditDTO productEditDTO) {

        if (!customExpressionService
                .canAccessProduct(productEditDTO.getId())) {

            throw new AccessDeniedException();
        }

        Product product = productMapper.toEntity(productEditDTO);
        Product updated = productService.update(product);
        ProductDTO productDTO = productMapper.toDetailedDTO(updated);

        OrganizationDTO organizationDTO =
                organizationClient.getSimpleById(
                        updated.getOrganizationId());
        productDTO.setOrganization(organizationDTO);

        Double avgRating = reviewClient
                .getAverageRatingByProductId(updated.getId());
        productDTO.setAverageRating(avgRating);


        return productDTO;
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByProductId(
            @PathVariable("id") Long id) {

        return reviewClient.getByProductId(id);
    }

    @PostMapping("/reviews")
    public ReviewDTO createReview(
            @RequestBody
            @Validated({OnCreate.class})
            ReviewPostDTO reviewPostDTO) {

        return reviewClient.create(reviewPostDTO);
    }


    @GetMapping("/{id}/simple")
    public ProductDTO getSimpleById(
            @PathVariable("id") Long id) {

        boolean statusCheck = false;

        Product product = productService.getById(id, statusCheck);

        return productMapper.toSimpleDTO(product);
    }

    @GetMapping("/{id}/price-quantity")
    public PriceQuantityResponse getPriceAndQuantityByProductId(
            @PathVariable("id") Long productId) {

        boolean statusCheck = true;

        Product product = productService.getById(productId, statusCheck);

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
                .products(productMapper.toSimpleListDTO(
                        organizationProducts))
                .build();
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable("id") Long id) {

        if (!customExpressionService.canAccessProduct(id)) {

            throw new AccessDeniedException();
        }
        return productService.markProductAsDeleted(id);
    }
}
