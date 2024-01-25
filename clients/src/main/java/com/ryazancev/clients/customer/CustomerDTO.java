package com.ryazancev.clients.customer;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerDTO {

    private Long id;

    private String username;
}
