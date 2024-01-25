package com.ryazancev.clients.organization;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationCreateDTO {

    private String name;
    private String description;
    private String logo;
}
