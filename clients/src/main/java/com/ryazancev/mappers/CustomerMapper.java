package com.ryazancev.mappers;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toDTO(Customer customer);
}
