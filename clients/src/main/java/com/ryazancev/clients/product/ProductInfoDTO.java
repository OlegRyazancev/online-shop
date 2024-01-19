package com.ryazancev.clients.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryazancev.clients.organization.OrganizationDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfoDTO {

    private Long id;

    private String name;

    private String description;

    private OrganizationDTO organization;

    private Double price;

    private Integer quantityInStock;

    @JsonProperty("keywords")
    private List<String> keywords;

    //todo: discount field

}
