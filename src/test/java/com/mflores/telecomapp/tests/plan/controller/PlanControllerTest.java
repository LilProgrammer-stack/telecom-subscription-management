package com.mflores.telecomapp.tests.plan.controller;

import com.mflores.telecomapp.controller.PlanController;
import com.mflores.telecomapp.dto.CreatePlanRequest;
import com.mflores.telecomapp.dto.PlanResponse;
import com.mflores.telecomapp.model.Money;
import com.mflores.telecomapp.service.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanController.class)
public class PlanControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanService planService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreatePlanSuccessfully() throws Exception {

        CreatePlanRequest planRequest = new CreatePlanRequest();
        planRequest.setPlanName("planName");
        planRequest.setDescription("planDescription");
        planRequest.setHotspotLimitMb(10000L);
        planRequest.setRoamingLimitMb(10000L);
        planRequest.setDataLimitMb(10000L);
        Money price = new Money(10000, Currency.getInstance("USD"));
        planRequest.setPrice(price);

        PlanResponse  planResponse = new PlanResponse();
        planResponse.setPlanId(1L);
        planResponse.setPlanName("planName");
        planResponse.setDescription("planDescription");
        planResponse.setHotspotLimitMb(10000L);
        planResponse.setRoamingLimitMb(10000L);
        planResponse.setDataLimitMb(10000L);
        Money price2 = new Money(10000, Currency.getInstance("USD"));
        planResponse.setPrice(price2);
        planResponse.setActive(true);

        when(planService.createPlan(any(CreatePlanRequest.class)))
                .thenReturn(planResponse);

        String json = objectMapper.writeValueAsString(planRequest);

        mockMvc.perform(post("/api/plan").with(user("testuser"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.planName").value("planName"))
                .andExpect(jsonPath("$.description").value("planDescription"))
                .andExpect(jsonPath("$.hotspotLimitMb").value(10000L))
                .andExpect(jsonPath("$.roamingLimitMb").value(10000L))
                .andExpect(jsonPath("$.dataLimitMb").value(10000L))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.price.amountInCents").value(10000))
                .andExpect(jsonPath("$.price.currency").value("USD"));

        verify(planService).createPlan(any(CreatePlanRequest.class));
    }

    @Test
    void shouldNotAllowInvalidInputs() throws Exception {

        CreatePlanRequest planRequest = new CreatePlanRequest();
        planRequest.setPlanName("");
        planRequest.setDescription("");
        planRequest.setHotspotLimitMb(-10000L);
        planRequest.setRoamingLimitMb(-10000L);
        planRequest.setDataLimitMb(-10000L);
        planRequest.setPrice(null);

        String json = objectMapper.writeValueAsString(planRequest);

        mockMvc.perform(post("/api/plan").with(user("testuser"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.planName").value("Plan name is required"))
                .andExpect(jsonPath("$.errors.description").value("A description is required"))
                .andExpect(jsonPath("$.errors.hotspotLimitMb").value("Hotspot cannot be negative"))
                .andExpect(jsonPath("$.errors.roamingLimitMb").value("Roaming cannot be negative"))
                .andExpect(jsonPath("$.errors.dataLimitMb").value("Data cannot be negative"))
                .andExpect(jsonPath("$.errors.price").value("A price is required"));

        verify(planService, never()).createPlan(any(CreatePlanRequest.class));

    }
}
