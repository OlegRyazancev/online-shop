package com.ryazancev.customer.util.mapper;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.customer.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toDTO(Customer customer);
}
