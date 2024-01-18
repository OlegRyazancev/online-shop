package com.ryazancev.customer.util.mappers;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.clients.customer.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toDTO(Customer customer);
}
