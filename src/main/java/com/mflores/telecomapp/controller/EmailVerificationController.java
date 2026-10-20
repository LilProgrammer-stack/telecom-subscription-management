package com.mflores.telecomapp.controller;

import com.mflores.telecomapp.service.EmailVerificationTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration/verify")
public class EmailVerificationController {

    private final EmailVerificationTokenService emailVerificationTokenService;

    public EmailVerificationController(EmailVerificationTokenService emailVerificationTokenService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    @PostMapping
    public ResponseEntity<Void> verifyEmail(@RequestParam String token){

        emailVerificationTokenService.verifyEmailToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> showVerificationConfirmation(@RequestParam String token){

        //This will be implemented once we connect to the frontend, the token won't be consumed here
        return ResponseEntity.ok().build();
    }
}
