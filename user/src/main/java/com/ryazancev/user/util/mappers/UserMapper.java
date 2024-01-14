package com.ryazancev.user.util.mappers;

import com.ryazancev.user.dto.UserDTO;
import com.ryazancev.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDTO toDTO(User user);
}
