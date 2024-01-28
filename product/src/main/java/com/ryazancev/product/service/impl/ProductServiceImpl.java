package com.ryazancev.product.service.impl;

import com.ryazancev.clients.organization.OrganizationClient;
import com.ryazancev.clients.organization.dto.OrganizationDTO;
import com.ryazancev.clients.product.dto.*;
import com.ryazancev.clients.review.ReviewClient;
import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewProductDTO;
import com.ryazancev.clients.review.dto.ReviewsProductResponse;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.InvalidQuantityException;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;
import com.ryazancev.product.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final OrganizationClient organizationClient;
    private final ReviewClient reviewClient;

    @Override
    public ProductListResponse getAll() {

        List<Product> products = productRepository.findAll();
        List<ProductSimpleDTO> productsDTO = productMapper.toListDTO(products);

        return ProductListResponse.builder()
                .products(productsDTO)
                .build();
    }

    @Override
    public ProductSimpleDTO getSimpleById(Long id) {

        Product existing = findById(id);

        return productMapper.toSimpleDTO(existing);
    }

    @Override
    public ProductDetailedDTO getDetailedById(Long id) {

        Product existing = findById(id);

        ProductDetailedDTO productDetailedDTO = productMapper
                .toDetailedDTO(existing);

        OrganizationDTO organization = organizationClient
                .getSimpleById(existing.getOrganizationId());
        productDetailedDTO.setOrganization(organization);

        ReviewsProductResponse response = reviewClient
                .getByProductId(existing.getId());
        List<ReviewProductDTO> reviews = response.getReviews();
        productDetailedDTO.setReviews(reviews);

        Double averageRating = calculateAverageRating(reviews);
        productDetailedDTO.setAverageRating(averageRating);

        return productDetailedDTO;
    }

    @Override
    public ProductListResponse getByOrganizationId(Long organizationId) {

        List<Product> products = productRepository
                .findByOrganizationId(organizationId);

        return ProductListResponse.builder()
                .products(productMapper.toListDTO(products))
                .build();
    }

    @Transactional
    @Override
    public ProductDetailedDTO create(ProductCreateDTO productCreateDTO) {

        if (productRepository.findByProductName(
                productCreateDTO.getProductName()).isPresent()) {
            throw new ProductCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        Product productToSave = productMapper.toEntity(productCreateDTO);
        Product savedProduct = productRepository.save(productToSave);

        return createProductDetailedDTO(savedProduct);
    }

    @Transactional
    @Override
    public ProductDetailedDTO updateQuantity(Long id, Integer quantity) {

        if (quantity < 0) {
            throw new InvalidQuantityException(
                    "Quantity can not be less than 0",
                    HttpStatus.BAD_REQUEST
            );
        }

        Product existing = findById(id);
        existing.setQuantityInStock(quantity);

        return productMapper.toDetailedDTO(existing);
    }

    @Transactional
    @Override
    public ProductDetailedDTO update(ProductUpdateDTO productUpdateDTO) {

        Product existing = findById(productUpdateDTO.getId());

        updateProductFields(existing, productUpdateDTO);

        Product savedProduct = productRepository.save(existing);

        return createProductDetailedDTO(savedProduct);
    }

    @Override
    public ReviewsProductResponse getReviewsByProductId(Long productId) {

        return reviewClient.getByProductId(productId);
    }

    @Override
    public ReviewDetailedDTO createReview(ReviewPostDTO reviewPostDTO) {

        return reviewClient.create(reviewPostDTO);
    }

    private Product findById(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found",
                        HttpStatus.NOT_FOUND
                ));
    }

    private ProductDetailedDTO createProductDetailedDTO(Product savedProduct) {

        ProductDetailedDTO savedProductDetailedDTO = productMapper
                .toDetailedDTO(savedProduct);
        OrganizationDTO productOrganization = organizationClient
                .getSimpleById(savedProduct.getOrganizationId());
        savedProductDetailedDTO.setOrganization(productOrganization);
        return savedProductDetailedDTO;
    }

    private Double calculateAverageRating(List<ReviewProductDTO> reviews) {

        return reviews.stream()
                .mapToDouble(ReviewProductDTO::getRating)
                .average()
                .orElse(0.0);
    }

    private void updateProductFields(Product existing,
                                     ProductUpdateDTO productUpdateDTO) {

        existing.setProductName(productUpdateDTO.getProductName());
        existing.setDescription(productUpdateDTO.getDescription());
        existing.setPrice(productUpdateDTO.getPrice());
        existing.setQuantityInStock(productUpdateDTO.getQuantityInStock());
        existing.setKeywords(String.join(
                ", ",
                productUpdateDTO.getKeywords()
        ));
    }
}
