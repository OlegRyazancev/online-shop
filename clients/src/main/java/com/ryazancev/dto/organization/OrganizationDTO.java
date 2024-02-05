package com.ryazancev.dto.organization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.product.ProductDTO;
import lombok.*;

import java.time.LocalDateTime;
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

    private String status;

    private LocalDateTime registeredAt;

}
