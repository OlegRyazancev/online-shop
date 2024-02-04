package com.ryazancev.admin.dto;

import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgRegRequestsResponse {


    private List<OrgRegRequestDTO> requests;
}
