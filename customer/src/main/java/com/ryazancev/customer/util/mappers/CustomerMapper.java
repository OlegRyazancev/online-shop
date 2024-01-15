package com.ryazancev.customer.util.mappers;

import com.ryazancev.customer.dto.CustomerDTO;
import com.ryazancev.customer.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {


    CustomerDTO toDTO(Customer customer);
}
