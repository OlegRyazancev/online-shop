package com.ryazancev.organization.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Getter
public class RequestHeader {
    private final Long userId;

    private final boolean locked;

    private final boolean confirmed;

    private final List<String> roles;

    public RequestHeader(HttpServletRequest request) {
        this.userId = Long.valueOf(
                request.getHeader("userId"));
        this.locked = Boolean.parseBoolean(
                request.getHeader("locked"));
        this.confirmed = Boolean.parseBoolean(
                request.getHeader("confirmed"));
        this.roles = Arrays
                .stream(request
                        .getHeader("roles")
                        .split(" "))
                .toList();
    }
}
