package com.ryazancev.user.service;

import com.ryazancev.user.model.User;

public interface UserService {
    User getById(Long userId);
}
