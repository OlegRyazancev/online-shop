package com.ryazancev.product.service.impl;

import com.ryazancev.clients.organization.OrganizationClient;
import com.ryazancev.clients.organization.dto.OrganizationDTO;
import com.ryazancev.clients.product.dto.ProductDTO;
import com.ryazancev.clients.product.dto.ProductEditDTO;
import com.ryazancev.clients.product.dto.ProductsSimpleListResponse;
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
    public ProductsSimpleListResponse getAll() {

        List<Product> products = productRepository.findAll();
        List<ProductDTO> productsDTO = productMapper.toSimpleListDTO(products);

        return ProductsSimpleListResponse.builder()
                .products(productsDTO)
                .build();
    }

    @Override
    public ProductDTO getSimpleById(Long id) {

        Product existing = findById(id);

        return productMapper.toSimpleDTO(existing);
    }

    @Override
    public ProductDTO getDetailedById(Long id) {

        Product existing = findById(id);

        ProductDTO productDTO = productMapper
                .toDetailedDTO(existing);

        OrganizationDTO organizationDTO = organizationClient
                .getSimpleById(existing.getOrganizationId());
        productDTO.setOrganization(organizationDTO);

        ReviewsProductResponse response = reviewClient
                .getByProductId(existing.getId());
        List<ReviewProductDTO> reviewsDTO = response.getReviews();
        productDTO.setReviews(reviewsDTO);

        Double averageRating = calculateAverageRating(reviewsDTO);
        productDTO.setAverageRating(averageRating);

        return productDTO;
    }

    @Override
    public ProductsSimpleListResponse getByOrganizationId(Long organizationId) {

        List<Product> products = productRepository
                .findByOrganizationId(organizationId);

        return ProductsSimpleListResponse.builder()
                .products(productMapper.toSimpleListDTO(products))
                .build();
    }

    @Transactional
    @Override
    public ProductDTO create(ProductEditDTO productEditDTO) {

        if (productRepository.findByProductName(
                productEditDTO.getProductName()).isPresent()) {
            throw new ProductCreationException(
                    "Organization with this name already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        Product toSave = productMapper.toEntity(productEditDTO);
        log.info("Org id: {}", toSave.getOrganizationId());
        Product saved = productRepository.save(toSave);

        return createProductDetailedDTO(saved);
    }

    @Transactional
    @Override
    public ProductDTO updateQuantity(Long id, Integer quantity) {

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
    public ProductDTO update(ProductEditDTO productEditDTO) {

        Product existing = findById(productEditDTO.getId());

        updateProductFields(existing, productEditDTO);

        Product saved = productRepository.save(existing);

        return createProductDetailedDTO(saved);
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

    private ProductDTO createProductDetailedDTO(Product product) {

        ProductDTO productDTO = productMapper
                .toDetailedDTO(product);
        OrganizationDTO organizationDTO = organizationClient
                .getSimpleById(product.getOrganizationId());
        productDTO.setOrganization(organizationDTO);
        return productDTO;
    }

    private Double calculateAverageRating(List<ReviewProductDTO> reviews) {

        return reviews.stream()
                .mapToDouble(ReviewProductDTO::getRating)
                .average()
                .orElse(0.0);
    }

    private void updateProductFields(Product product,
                                     ProductEditDTO productEditDTO) {

        product.setProductName(productEditDTO.getProductName());
        product.setDescription(productEditDTO.getDescription());
        product.setPrice(productEditDTO.getPrice());
        product.setQuantityInStock(productEditDTO.getQuantityInStock());
        product.setKeywords(String.join(
                ", ",
                productEditDTO.getKeywords()
        ));
    }
}
