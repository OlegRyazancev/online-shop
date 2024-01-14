package com.ryazancev.user.service.impl;

import com.ryazancev.user.model.User;
import com.ryazancev.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public User getById(Long userId) {
        return User.builder()
                .id(userId)
                .username("Oleg")
                .email("oleg@gmail.com")
                .balance(213.0)
                .build();
    }
}
