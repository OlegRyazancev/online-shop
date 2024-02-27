package com.ryazancev.auth.security.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Oleg Ryazancev
 */

@Data
@AllArgsConstructor
public class JwtEntity implements UserDetails {

    private Long id;
    private final String name;
    private final String username;
    private final String password;
    private final Long customerId;
    private final Collection<? extends GrantedAuthority> authorities;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
