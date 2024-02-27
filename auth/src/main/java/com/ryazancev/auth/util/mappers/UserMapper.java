package com.ryazancev.auth.util.mappers;

import com.ryazancev.auth.model.User;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @Mapping(target = "username", source = "name")
    @Mapping(target = "balance", constant = "0.00")
    CustomerDto toCustomerDto(User user);
}
