package com.ryazancev.product.util.processor;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.product.ProductsSimpleResponse;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DtoProcessor {

    private final ProductMapper productMapper;
    private final ClientsService clientsService;
    private final ProductService productService;

    public ProductsSimpleResponse createProductsSimpleResponse(
            List<Product> products) {

        return ProductsSimpleResponse.builder()
                .products(productMapper.toSimpleListDto(products))
                .build();
    }

    public ProductDto createProductDetailedDtoWithOrganizationAndAvgRating(
            Product product) {

        ProductDto productDto =
                createProductDetailedDtoWithOrganization(product);

        productDto.setAverageRating(
                clientsService.getAverageRatingByProductId(
                        product.getId()));

        return productDto;
    }

    public ProductDto createProductDetailedDtoWithOrganization(
            Product product) {

        ProductDto productDto = productMapper.toDetailedDto(product);

        productDto.setOrganization(
                clientsService.getSimpleOrganizationById(
                        product.getOrganizationId()));

        return productDto;
    }

    public ReviewsResponse getReviewsResponseByProductId(Long id) {

        return (ReviewsResponse) clientsService.getReviewsByProductId(id);
    }

    public ReviewDto createReviewDto(ReviewEditDto reviewEditDto) {

        return (ReviewDto) clientsService.createReview(reviewEditDto);
    }

    public ProductDto createProductSimpleDto(Product product) {

        return productMapper.toSimpleDto(product);
    }

    public PriceQuantityResponse createPriceQuantityResponse(Product product) {

        return PriceQuantityResponse.builder()
                .price(product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .build();
    }

    public Long getOrganizationOwnerDtoById(Long organizationId) {

        return (Long) clientsService
                .getOrganizationOwnerIdById(organizationId);
    }

    public MailDto createMailDto(Long productId,
                                  MailType mailType) {

        boolean statusCheck = false;
        Product registered = productService.getById(
                productId, statusCheck);

        Long customerId = (Long) clientsService
                .getOrganizationOwnerIdById(registered.getOrganizationId());

        CustomerDto customerDto = clientsService
                .getSimpleCustomerById(customerId)
                .safelyCast(CustomerDto.class, true);

        Properties properties = new Properties();

        properties.setProperty(
                "object_name", registered.getProductName());
        properties.setProperty(
                "object_type", ObjectType.PRODUCT.name().toLowerCase());

        return MailDto.builder()
                .email(customerDto.getEmail())
                .type(mailType)
                .name(customerDto.getUsername())
                .properties(properties)
                .build();
    }
}
