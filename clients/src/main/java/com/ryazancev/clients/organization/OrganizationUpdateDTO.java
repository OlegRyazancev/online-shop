package com.ryazancev.clients.organization;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationUpdateDTO {

    private Long id;
    private String name;
    private String description;
    private String logo;
}
