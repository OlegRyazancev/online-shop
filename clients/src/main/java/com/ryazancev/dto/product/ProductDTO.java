package com.ryazancev.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.review.ReviewDTO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO implements Serializable {

    private Long id;

    private String productName;

    private String description;

    private OrganizationDTO organization;

    private Double price;

    private Integer quantityInStock;

    private List<String> keywords;

    private List<ReviewDTO> reviews;

    private Double averageRating;

    private String status;

    private LocalDateTime registeredAt;

    //todo: discount field

}
