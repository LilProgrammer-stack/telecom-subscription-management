package com.mflores.telecomapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    @GetMapping("/api/auth/csrf")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {

        return ResponseEntity.ok(csrfToken);
    }
}
