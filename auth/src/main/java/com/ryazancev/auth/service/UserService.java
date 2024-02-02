package com.ryazancev.auth.service;

import com.ryazancev.auth.model.User;
import com.ryazancev.dto.user.UserDTO;

public interface UserService {

    UserDTO create(UserDTO userDTO);

    User getByEmail(String username);

    User getById(Long id);
}
