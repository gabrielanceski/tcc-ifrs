package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.impl.UserAuthenticated;
import com.gabrielanceski.tccifrs.infrastructure.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public record UserDetailsServiceImpl(UserRepository userRepository) implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String document) throws UsernameNotFoundException {
        return userRepository.findByDocument(document)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
