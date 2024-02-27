package com.ryazancev.customer.util.mapper;

import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {

    @Mapping(target = "balance", ignore = true)
    CustomerDto toSimpleDto(Customer customer);

    CustomerDto toDetailedDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);
}
