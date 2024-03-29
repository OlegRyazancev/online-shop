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

    public ProductsSimpleResponse createProductsSimpleResponse(
            final List<Product> products) {

        return ProductsSimpleResponse.builder()
                .products(productMapper.toSimpleListDto(products))
                .build();
    }

    public ProductDto createProductDetailedDtoWithOrganizationAndAvgRating(
            final Product product) {

        ProductDto productDto =
                createProductDetailedDtoWithOrganization(product);

        productDto.setAverageRating(
                clientsService.getAverageRatingByProductId(
                        product.getId()));

        return productDto;
    }

    public ProductDto createProductDetailedDtoWithOrganization(
            final Product product) {

        ProductDto productDto = productMapper.toDetailedDto(product);

        productDto.setOrganization(
                clientsService.getSimpleOrganizationById(
                        product.getOrganizationId()));

        return productDto;
    }

    public ReviewsResponse getReviewsResponseByProductId(
            final Long id) {

        return (ReviewsResponse) clientsService.getReviewsByProductId(id);
    }

    public ReviewDto createReviewDto(
            final ReviewEditDto reviewEditDto) {

        return (ReviewDto) clientsService.createReview(reviewEditDto);
    }

    public ProductDto createProductSimpleDto(
            final Product product) {

        return productMapper.toSimpleDto(product);
    }

    public PriceQuantityResponse createPriceQuantityResponse(
            final Product product) {

        return PriceQuantityResponse.builder()
                .price(product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .build();
    }

    public Long getOrganizationOwnerId(
            final Long organizationId) {

        return (Long) clientsService
                .getOwnerByOrganizationId(organizationId);
    }

    public MailDto createMailDto(final Product product,
                                 final MailType mailType) {

        Long customerId = (Long) clientsService
                .getOwnerByOrganizationId(product.getOrganizationId());

        CustomerDto customerDto = clientsService
                .getSimpleCustomerById(customerId)
                .safelyCast(CustomerDto.class, true);

        Properties properties = new Properties();

        properties.setProperty(
                "object_name", product.getProductName());
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
