package com.ryazancev.customer.util.mapper;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "balance", ignore = true)
    CustomerDTO toSimple(Customer customer);

    CustomerDTO toDetailedDTO(Customer customer);

    Customer toEntity(CustomerDTO customerDTO);
}
