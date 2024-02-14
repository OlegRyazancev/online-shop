package com.ryazancev.dto.organization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.dto.customer.CustomerDto;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDto {

    private Long id;

    private String name;

    private String description;

    private String logo;

    private CustomerDto owner;

    private String status;

    private LocalDateTime registeredAt;

}
