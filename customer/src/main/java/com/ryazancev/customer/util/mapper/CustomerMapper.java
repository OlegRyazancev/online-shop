package com.ryazancev.customer.util.mapper;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerDetailedDTO;
import com.ryazancev.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {

    CustomerDTO toDTO(Customer customer);

    CustomerDetailedDTO toDetailedDTO(Customer customer);
}
