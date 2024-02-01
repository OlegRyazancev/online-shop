package com.ryazancev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
