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
import com.ryazancev.dto.CustomerDTO;
import com.ryazancev.dto.MailDTO;
import com.ryazancev.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.kafka.topic}")
    private String topicName;
    private final KafkaTemplate<String, MailDTO> kafkaTemplate;

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

        log.info("User email {}", user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmed(false);
        user.setLocked(false);

        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);

        CustomerDTO createdCustomer =
                customerClient.createCustomer(
                        userMapper.toCustomerDTO(user));
        user.setCustomerId(createdCustomer.getId());


        User saved = userRepository.save(user);

        ConfirmationToken token = AuthUtil
                .getConfirmationToken(saved);
        confirmationTokenService.save(token);


        MailDTO mailDTO = AuthUtil
                .createConfirmationMailDTO(
                        saved.getEmail(),
                        saved.getName(),
                        token.getToken());

        kafkaTemplate.send(topicName, mailDTO);

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
