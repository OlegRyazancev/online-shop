package com.ryazancev.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationsSimpleResponse {
    private List<OrganizationDTO> organizations;
}
