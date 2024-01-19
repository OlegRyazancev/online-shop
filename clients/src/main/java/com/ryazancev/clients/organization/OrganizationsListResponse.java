package com.ryazancev.clients.organization;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationsListResponse {
    private List<OrganizationDTO> organizations;
}
