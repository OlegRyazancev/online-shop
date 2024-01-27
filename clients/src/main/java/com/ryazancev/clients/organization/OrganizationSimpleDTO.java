package com.ryazancev.clients.organization;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationSimpleDTO {

    private Long id;
    private String name;
}
