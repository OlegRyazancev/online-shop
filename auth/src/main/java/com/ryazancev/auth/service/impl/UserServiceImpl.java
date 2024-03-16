package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.kafka.AuthProducerService;
import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.Role;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.service.ClientsService;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.service.UserService;
import com.ryazancev.auth.util.AuthUtil;
import com.ryazancev.auth.util.exception.custom.UserNotFoundException;
import com.ryazancev.auth.util.mappers.UserMapper;
import com.ryazancev.auth.util.validator.AuthValidator;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.user.UserDto;
import com.ryazancev.common.dto.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;


/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthUtil authUtil;
    private final AuthValidator authValidator;
    private final AuthProducerService authProducerService;

    private final ClientsService clientsService;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final ConfirmationTokenService confirmationTokenService;

    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;


    @Transactional
    @Override
    public UserDto create(UserDto userDto) {

        authValidator.validateEmailUniqueness(userDto);
        authValidator.validatePasswordConfirmation(userDto);

        User user = userMapper.toEntity(userDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmed(false);
        user.setLocked(false);

        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);

        CustomerDto createdCustomer =
                (CustomerDto) clientsService.createCustomer(
                        userMapper.toCustomerDto(user));
        user.setCustomerId(createdCustomer.getId());


        User saved = userRepository.save(user);

        ConfirmationToken token = authUtil.getConfirmationToken(saved);
        confirmationTokenService.save(token);


        MailDto mailDto = authUtil.createConfirmationMailDto(
                saved.getEmail(),
                saved.getName(),
                token.getToken());

        authProducerService.sendMessageToMailTopic(mailDto);

        return userMapper.toDto(saved);
    }


    @Override
    public User getByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                messageSource.getMessage(
                                        "exception.auth.user_not_found_by_email",
                                        new Object[]{email},
                                        Locale.getDefault()
                                ),
                                HttpStatus.NOT_FOUND));
    }

    @Override
    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                messageSource.getMessage(
                                        "exception.auth.user_not_found_by_id",
                                        new Object[]{id},
                                        Locale.getDefault()
                                ),
                                HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public void toggleUserLock(Long id, boolean lock) {

        User existing = getById(id);

        existing.setLocked(lock);

        userRepository.save(existing);
    }

    @Override
    @Transactional
    public void markUserAsDeletedByCustomerId(Long customerId) {

        User existing = userRepository
                .findByCustomerId(customerId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                messageSource.getMessage(
                                        "exception.auth.user_not_found_by_customer_id",
                                        new Object[]{customerId},
                                        Locale.getDefault()
                                ),
                                HttpStatus.NOT_FOUND
                        ));

        existing.setName("DELETED");
        existing.setDeletedAt(LocalDateTime.now());

        userRepository.save(existing);
    }

    @Override
    @Transactional
    public void updateByCustomer(UserUpdateRequest request) {

        User existing = userRepository
                .findByCustomerId(request.getCustomerId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                messageSource.getMessage(
                                        "exception.auth.user_not_found_by_customer_id",
                                        new Object[]{request.getCustomerId()},
                                        Locale.getDefault()
                                ),
                                HttpStatus.NOT_FOUND));

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());

        userRepository.save(existing);
    }
}
