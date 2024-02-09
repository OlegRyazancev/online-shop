package com.ryazancev.dto.organization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.customer.CustomerDTO;
import lombok.*;

import java.time.LocalDateTime;

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

    private CustomerDTO owner;

    private String status;

    private LocalDateTime registeredAt;

}
