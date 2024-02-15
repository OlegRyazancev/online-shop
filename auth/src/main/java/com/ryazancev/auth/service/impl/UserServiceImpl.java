package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.Role;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.auth.util.AuthUtil;
import com.ryazancev.auth.util.exception.custom.UserCreationException;
import com.ryazancev.auth.util.exception.custom.UserNotFoundException;
import com.ryazancev.auth.util.mappers.UserMapper;
import com.ryazancev.clients.CustomerClient;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.mail.MailDto;
import com.ryazancev.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CustomerClient customerClient;
    private final ConfirmationTokenService confirmationTokenService;
    private final AuthUtil authUtil;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.kafka.topic.mail}")
    private String mailTopicName;

    private final KafkaTemplate<String, MailDto> kafkaTemplate;

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {

        if (userRepository.findByEmail(
                userDto.getEmail()).isPresent()) {
            throw new UserCreationException(
                    "User with this email already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!userDto.getPassword().equals(
                userDto.getPasswordConfirmation())) {
            throw new UserCreationException(
                    "Password and password confirmation do not equals",
                    HttpStatus.BAD_REQUEST
            );
        }

        User user = userMapper.toEntity(userDto);

        log.info("User email {}", user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmed(false);
        user.setLocked(false);

        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);

        CustomerDto createdCustomer =
                customerClient.createCustomer(
                        userMapper.toCustomerDto(user));
        user.setCustomerId(createdCustomer.getId());


        User saved = userRepository.save(user);

        ConfirmationToken token = authUtil.getConfirmationToken(saved);
        confirmationTokenService.save(token);


        MailDto mailDto = authUtil.createConfirmationMailDto(
                saved.getEmail(),
                saved.getName(),
                token.getToken());

        kafkaTemplate.send(mailTopicName, mailDto);

        return userMapper.toDto(saved);
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

    @Override
    @Transactional
    public void toggleUserLock(String username, boolean lock) {

        User existing = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found",
                                HttpStatus.NOT_FOUND));

        existing.setLocked(lock);

        userRepository.save(existing);
    }

    @Override
    @Transactional
    public void markUserAsDeletedByCustomerId(Long customerId) {

        User existing = userRepository.findByCustomerId(customerId).
                orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));

        existing.setName("DELETED");
        existing.setDeletedAt(LocalDateTime.now());

        userRepository.save(existing);
    }
}
