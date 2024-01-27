package com.ryazancev.clients.organization;

import com.ryazancev.clients.product.ProductSimpleDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationDetailedDTO {

    private Long id;
    private String name;
    private String description;
    private String logo;
    private List<ProductSimpleDTO> products;
}
