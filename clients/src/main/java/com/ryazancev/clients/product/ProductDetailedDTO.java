package com.ryazancev.clients.product;

import com.ryazancev.clients.organization.OrganizationSimpleDTO;
import com.ryazancev.clients.review.ReviewProductDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailedDTO {

    private Long id;

    private String productName;

    private String description;

    private OrganizationSimpleDTO organization;

    private Double price;

    private Integer quantityInStock;

    private List<String> keywords;

    private List<ReviewProductDTO> reviews;

    private Double averageRating;

    //todo: discount field

}
