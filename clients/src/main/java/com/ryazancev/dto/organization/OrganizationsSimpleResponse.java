package com.ryazancev.dto.organization;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationsSimpleResponse {
    private List<OrganizationDto> organizations;
}
