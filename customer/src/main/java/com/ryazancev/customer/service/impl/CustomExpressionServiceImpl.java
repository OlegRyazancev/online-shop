package com.ryazancev.customer.service.impl;

import com.ryazancev.customer.service.CustomExpressionService;
import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.ryazancev.customer.util.exception.Message.*;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;

    @Override
    public void checkAccountConditions() {

        checkIfAccountLocked();
        checkIfEmailConfirmed();
    }

    @Override
    public void checkAccessCustomer(Long customerId) {

        if (!canAccessCustomer(customerId)) {

            throw new AccessDeniedException(
                    ACCESS_CUSTOMER,
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkIfAccountLocked() {

        boolean locked = Boolean.parseBoolean(request.getHeader("locked"));

        if (locked) {
            throw new AccessDeniedException(
                    ACCOUNT_LOCKED,
                    HttpStatus.FORBIDDEN);

        }
    }

    private void checkIfEmailConfirmed() {

        boolean confirmed = Boolean.parseBoolean(
                request.getHeader("confirmed"));

        if (!confirmed) {
            throw new AccessDeniedException(
                    EMAIL_NOT_CONFIRMED,
                    HttpStatus.FORBIDDEN);
        }
    }

    private boolean canAccessCustomer(Long id) {

        Long userId = Long.valueOf(request.getHeader("userId"));
        List<String> userRoles = Arrays
                .stream(request
                        .getHeader("roles")
                        .split(" ")).toList();

        return userId.equals(id) || userRoles.contains("ROLE_ADMIN");
    }


}
