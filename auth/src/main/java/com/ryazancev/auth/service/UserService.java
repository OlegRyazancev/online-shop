package com.ryazancev.auth.service;

import com.ryazancev.auth.dto.UserDTO;
import com.ryazancev.auth.model.User;

public interface UserService {

    UserDTO create(UserDTO userDTO);

    User getByEmail(String username);

    User getById(Long id);
}
