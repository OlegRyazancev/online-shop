package com.ryazancev.auth.security.jwt;

import com.ryazancev.auth.model.Role;
import com.ryazancev.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Ryazancev
 */

public class JwtEntityFactory {

    public static JwtEntity create(User user) {

        return new JwtEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getCustomerId(),
                mapToGrantedAuthorities(
                        new ArrayList<>(user.getRoles())
                )
        );
    }

    public static List<GrantedAuthority> mapToGrantedAuthorities(
            List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
