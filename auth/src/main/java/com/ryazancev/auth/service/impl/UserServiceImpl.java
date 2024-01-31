package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.dto.UserDTO;
import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.Role;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.auth.util.UserMapper;
import com.ryazancev.auth.util.exception.custom.UserCreationException;
import com.ryazancev.auth.util.exception.custom.UserNotFoundException;
import com.ryazancev.clients.customer.CustomerClient;
import com.ryazancev.clients.customer.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CustomerClient customerClient;
    private final ConfirmationTokenService confirmationTokenService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDTO create(UserDTO userDTO) {

        if (userRepository.findByEmail(
                userDTO.getEmail()).isPresent()) {
            throw new UserCreationException(
                    "User with this email already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!userDTO.getPassword().equals(
                userDTO.getPasswordConfirmation())) {
            throw new UserCreationException(
                    "Password and password confirmation do not equals",
                    HttpStatus.BAD_REQUEST
            );
        }

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmed(false);
        user.setLocked(false);

        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);

        CustomerDTO customerDTO = CustomerDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .balance(0.00)
                .build();
        CustomerDTO createdCustomer =
                customerClient.createCustomer(customerDTO);
        user.setCustomerId(createdCustomer.getId());

        User saved = userRepository.save(user);


        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(saved)
                .build();

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        //todo: sendEmail

        return userMapper.toDTO(saved);
    }

    @Override
    public User getByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found",
                                HttpStatus.NOT_FOUND));
    }

    @Override
    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found",
                                HttpStatus.NOT_FOUND));
    }

}
