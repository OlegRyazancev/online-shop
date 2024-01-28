package com.ryazancev.clients.customer.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerDetailedDTO {

    private Long id;

    private String username;

    private String email;

    private Double balance;

}
