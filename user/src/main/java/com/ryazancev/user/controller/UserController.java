package com.ryazancev.user.controller;

import com.ryazancev.user.dto.UserDTO;
import com.ryazancev.user.model.User;
import com.ryazancev.user.service.UserService;
import com.ryazancev.user.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        log.info(String.valueOf(user));
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

}
