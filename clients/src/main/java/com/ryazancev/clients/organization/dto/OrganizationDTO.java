package com.ryazancev.clients.organization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.product.dto.ProductDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDTO {

    private Long id;

    private String name;

    private String description;

    private String logo;

    private List<ProductDTO> products;

    private CustomerDTO owner;
}
