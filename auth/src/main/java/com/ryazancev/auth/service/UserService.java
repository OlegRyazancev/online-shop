package com.ryazancev.auth.service;

import com.ryazancev.auth.model.User;
import com.ryazancev.common.dto.user.UserDto;
import com.ryazancev.common.dto.user.UserUpdateRequest;

/**
 * @author Oleg Ryazancev
 */

public interface UserService {

    UserDto create(UserDto userDto);

    User getByEmail(String username);

    User getById(Long id);

    void toggleUserLock(Long id, boolean lock);

    void markUserAsDeletedByCustomerId(Long customerId);

    void updateByCustomer(UserUpdateRequest request);
}
