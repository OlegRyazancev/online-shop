package com.ryazancev.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.organization.OrganizationDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    private Long id;

    private String productName;

    private String description;

    private OrganizationDto organization;

    private Double price;

    private Integer quantityInStock;

    private List<String> keywords;

    private Double averageRating;

    private String status;

    private LocalDateTime registeredAt;

}