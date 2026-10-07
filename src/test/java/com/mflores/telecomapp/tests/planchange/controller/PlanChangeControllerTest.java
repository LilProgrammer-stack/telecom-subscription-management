package com.mflores.telecomapp.tests.planchange.controller;

import com.mflores.telecomapp.controller.PlanChangeController;
import com.mflores.telecomapp.dto.PlanChangeRequestEntity;
import com.mflores.telecomapp.dto.PlanChangeResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.PhoneLine;
import com.mflores.telecomapp.model.Plan;
import com.mflores.telecomapp.model.PlanChangeStatus;
import com.mflores.telecomapp.service.PhoneLineService;
import com.mflores.telecomapp.service.PlanChangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanChangeController.class)
public class PlanChangeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PlanChangeService planChangeService;
    @MockitoBean
    private PhoneLineService phoneLineService;

    @Test
    void shouldPerformPlanChange() throws Exception {

        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(2L);
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneLineId(1L);

        Plan  currentPlan = new Plan();
        currentPlan.setPlanName("Basic");
        Plan  requestedPlan = new Plan();
        requestedPlan.setPlanName("Advanced");
        PlanChangeResponse  planChangeResponse = new PlanChangeResponse();
        planChangeResponse.setCurrentPlan(currentPlan);
        planChangeResponse.setRequestedPlan(requestedPlan);
        planChangeResponse.setStatus(PlanChangeStatus.PENDING);
        planChangeResponse.setRequestedAt(OffsetDateTime.of(2003, 05, 13, 04,17, 45,04, ZoneOffset.UTC));
        planChangeResponse.setEffectiveAt(OffsetDateTime.of(2003, 06, 13, 00,00, 00,00, ZoneOffset.UTC));
        planChangeResponse.setPhoneLineId(phoneLine.getPhoneLineId());

        when(phoneLineService.findPhoneLineEntityById(phoneLine.getPhoneLineId()))
                .thenReturn(phoneLine);
        when(planChangeService.performPlanChange(planChangeRequestEntity, phoneLine))
                .thenReturn(planChangeResponse);

        String json = objectMapper.writeValueAsString(planChangeRequestEntity);

        mockMvc.perform(post("/api/phone-lines/{phoneLineId}/plan-changes",  phoneLine.getPhoneLineId())
                .with(user("testuser"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currentPlan.planName").value("Basic"))
                .andExpect(jsonPath("$.requestedPlan.planName").value("Advanced"))
                .andExpect(jsonPath("$.status").value(PlanChangeStatus.PENDING.toString()))
                .andExpect(jsonPath("$.requestedAt").value("2003-05-13T04:17:45.000000004Z"))
                .andExpect(jsonPath("$.effectiveAt").value("2003-06-13T00:00:00Z"))
                .andExpect(jsonPath("$.phoneLineId").value(1L));

        verify(phoneLineService).findPhoneLineEntityById(phoneLine.getPhoneLineId());
        verify(planChangeService).performPlanChange(planChangeRequestEntity, phoneLine);

    }

    @Test
    void shouldReturnBadRequestWhenPlanIdIsNull() throws Exception {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneLineId(1L);
        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(null);

        String json = objectMapper.writeValueAsString(planChangeRequestEntity);

        mockMvc.perform(post("/api/phone-lines/{phoneLineId}/plan-changes", phoneLine.getPhoneLineId())
                .with(user("testuser"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors.planId").value("A plan ID is required"))
                .andExpect(jsonPath("$.status").value(400));

        verify(planChangeService, never()).performPlanChange(planChangeRequestEntity, phoneLine);

    }

    @Test
    void shouldReturnNotFoundWhenPhoneLineDoesNotExist() throws Exception {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneLineId(100000000000L);
        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(2L);

        when(phoneLineService.findPhoneLineEntityById(phoneLine.getPhoneLineId()))
                .thenThrow(new ResourceNotFoundException(
                "Phone line not found with id: " + phoneLine.getPhoneLineId()
        ));

        String json = objectMapper.writeValueAsString(planChangeRequestEntity);

        mockMvc.perform(post("/api/phone-lines/{phoneLineId}/plan-changes", phoneLine.getPhoneLineId())
                .with(user("testuser"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.errors").value(nullValue()))
                .andExpect(jsonPath("$.status").value(404));

        verify(phoneLineService).findPhoneLineEntityById(phoneLine.getPhoneLineId());
        verify(planChangeService, never()).performPlanChange(planChangeRequestEntity, phoneLine);

    }
}
