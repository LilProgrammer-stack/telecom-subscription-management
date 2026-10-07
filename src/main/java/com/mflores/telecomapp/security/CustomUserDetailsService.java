package com.mflores.telecomapp.security;

import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.repository.CustomerCredentialRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerCredentialRepository customerCredentialRepository;

    public CustomUserDetailsService(CustomerCredentialRepository customerCredentialRepository) {
        this.customerCredentialRepository = customerCredentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomerCredential foundCredential = customerCredentialRepository.findByAccountOwnerEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("The customer credential could not be found."));

        return new CustomUserDetails(foundCredential);
    }


}
