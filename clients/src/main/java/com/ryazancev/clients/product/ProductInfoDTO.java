package com.ryazancev.clients.product;

import com.ryazancev.clients.organization.OrganizationDTO;
import lombok.*;

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

    //todo: discount field

}
