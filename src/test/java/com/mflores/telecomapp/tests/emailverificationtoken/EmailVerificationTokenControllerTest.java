package com.mflores.telecomapp.tests.emailverificationtoken;

import com.mflores.telecomapp.controller.EmailVerificationController;
import com.mflores.telecomapp.exception.BusinessRuleViolationException;
import com.mflores.telecomapp.service.EmailVerificationTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailVerificationController.class)
public class EmailVerificationTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private EmailVerificationTokenService emailVerificationTokenService;

    @Test
    void shouldReturnOkStatusWhenEmailVerificationIsSuccessful() throws Exception {

        mockMvc.perform(post("/api/registration/verify")
                        .param("token", "Tk123456")
                        .with(user("usertest"))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(emailVerificationTokenService).verifyEmailToken("Tk123456");
    }

    @Test
    void shouldReturnConflictWhenEmailVerificationTokenIsInvalid() throws Exception {

        doThrow(new BusinessRuleViolationException("Token does not exist"))
                .when(emailVerificationTokenService)
                .verifyEmailToken("InvalidToken");

        mockMvc.perform(post("/api/registration/verify")
                        .param("token", "InvalidToken")
                        .with(user("usertest"))
                        .with(csrf()))
                .andExpect(status().isConflict());

        verify(emailVerificationTokenService).verifyEmailToken("InvalidToken");
    }
}
