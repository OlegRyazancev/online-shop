package com.ryazancev.product.service.impl;

import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductEditDTO;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.dto.review.ReviewsResponse;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.service.ProductService;
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
    public ProductsSimpleResponse getAll() {

        List<Product> products = productRepository.findAll();
        List<ProductDTO> productsDTO = productMapper.toSimpleListDTO(products);

        return ProductsSimpleResponse.builder()
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

        ReviewsResponse response = reviewClient
                .getByProductId(existing.getId());
        List<ReviewDTO> reviewsDTO = response.getReviews();
        productDTO.setReviews(reviewsDTO);

        Double averageRating = calculateAverageRating(reviewsDTO);
        productDTO.setAverageRating(averageRating);

        return productDTO;
    }

    @Override
    public ProductsSimpleResponse getByOrganizationId(Long organizationId) {

        List<Product> products = productRepository
                .findByOrganizationId(organizationId);

        return ProductsSimpleResponse.builder()
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
    public ProductDTO update(ProductEditDTO productEditDTO) {

        Product existing = findById(productEditDTO.getId());

        updateProductFields(existing, productEditDTO);

        Product saved = productRepository.save(existing);

        return createProductDetailedDTO(saved);
    }

    @Override
    public ReviewsResponse getReviewsByProductId(Long productId) {

        return reviewClient.getByProductId(productId);
    }

    @Override
    public ReviewDTO createReview(ReviewPostDTO reviewPostDTO) {

        return reviewClient.create(reviewPostDTO);
    }

    @Transactional
    @Override
    public void updateQuantity(UpdateQuantityRequest request) {

        Product existing = findById(request.getProductId());
        existing.setQuantityInStock(request.getQuantityInStock());
        productRepository.save(existing);
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

    private Double calculateAverageRating(List<ReviewDTO> reviews) {

        return reviews.stream()
                .mapToDouble(ReviewDTO::getRating)
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
