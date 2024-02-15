package com.ryazancev.auth.service;

import com.ryazancev.auth.model.User;
import com.ryazancev.dto.user.UserDto;

public interface UserService {

    UserDto create(UserDto userDto);

    User getByEmail(String username);

    User getById(Long id);

    void toggleUserLock(String username, boolean lock);

    void markUserAsDeletedByCustomerId(Long customerId);
}
