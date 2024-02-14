package com.ryazancev.customer.service.expression.impl;

import com.ryazancev.customer.service.expression.CustomExpressionService;
import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;


    @Override
    public void checkAccessCustomer(Long customerId) {

        if (!canAccessCustomer(customerId)) {

            throw new AccessDeniedException(
                    "You have no permissions to access to this customer",
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkIfAccountLocked() {

        boolean locked = Boolean.parseBoolean(request.getHeader("locked"));

        if (locked) {
            throw new AccessDeniedException(
                    "Access denied because your account is locked",
                    HttpStatus.FORBIDDEN);

        }
    }

    private boolean canAccessCustomer(Long id) {

        Long userId = Long.valueOf(request.getHeader("userId"));
        List<String> userRoles = Arrays
                .stream(request
                        .getHeader("roles")
                        .split(" ")).toList();

        log.info("user Id = {}, user roles = {}", userId, userRoles);

        return userId.equals(id) || userRoles.contains("ROLE_ADMIN");
    }


}
