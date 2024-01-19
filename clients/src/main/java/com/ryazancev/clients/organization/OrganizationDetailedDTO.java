package com.ryazancev.clients.organization;

import com.ryazancev.clients.product.ProductDTO;
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
    private List<ProductDTO> products;
}
