package com.gabrielanceski.tccifrs.domain.impl;

import com.gabrielanceski.tccifrs.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class AuthenticatedUser implements UserDetails {
    private final User entity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> entity.getRole().name());
    }

    @Override
    public String getPassword() {
        return entity.getPassword();
    }

    @Override
    public String getUsername() {
        return entity.getDocument();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !entity.getBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return entity.getActive();
    }

    public User getEntity() {
        return entity;
    }
}
