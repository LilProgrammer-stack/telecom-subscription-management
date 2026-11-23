package com.mflores.telecomapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Service;
import com.mflores.telecomapp.dto.CustomerLoginRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Service
public class CustomerAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final CookieCsrfTokenRepository cookieCsrfTokenRepository;

    public CustomerAuthenticationService(AuthenticationManager authenticationManager,
                                         SecurityContextRepository securityContextRepository,
                                         SessionAuthenticationStrategy sessionAuthenticationStrategy,
                                         CookieCsrfTokenRepository cookieCsrfTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
        this.cookieCsrfTokenRepository = cookieCsrfTokenRepository;
    }

    public Authentication authenticateCustomer(CustomerLoginRequest request, HttpServletRequest httpServletRequest,
                                               HttpServletResponse httpServletResponse) {

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.getEmail(),
                        request.getPassword()
                );

        Authentication authentication = authenticationManager.authenticate(authenticationRequest);

        sessionAuthenticationStrategy.onAuthentication(
                authentication,
                httpServletRequest,
                httpServletResponse
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
        return authentication;
    }
}