package com.ryazancev.customer.service.expression.impl;

import com.ryazancev.customer.service.expression.CustomExpressionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;

    @Override
    public boolean canAccessCustomer(Long id) {

        Long userId = Long.valueOf(request.getHeader("userId"));
        List<String> userRoles = Arrays
                .stream(request
                        .getHeader("roles")
                        .split(" ")).toList();

        log.info("user Id = {}, user roles = {}", userId, userRoles);

        return userId.equals(id) || userRoles.contains("ROLE_ADMIN");
    }
}
