package com.ryazancev.auth.util.mappers;

import com.ryazancev.auth.model.User;
import com.ryazancev.dto.CustomerDTO;
import com.ryazancev.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    @Mapping(target = "username", source = "name")
    @Mapping(target = "balance", constant = "0.00")
    CustomerDTO toCustomerDTO(User user);
}
