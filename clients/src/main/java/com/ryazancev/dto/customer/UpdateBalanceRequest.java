package com.ryazancev.dto.customer;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateBalanceRequest {

    private Long customerId;
    private Double balance;
}
