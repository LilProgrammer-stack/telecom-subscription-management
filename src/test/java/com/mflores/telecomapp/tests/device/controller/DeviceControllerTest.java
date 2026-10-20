package com.mflores.telecomapp.tests.device.controller;


import com.mflores.telecomapp.controller.DeviceController;
import com.mflores.telecomapp.dto.AddDeviceRequest;
import com.mflores.telecomapp.dto.DeviceAddedResponse;
import com.mflores.telecomapp.model.BrandType;
import com.mflores.telecomapp.model.Money;
import com.mflores.telecomapp.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceService deviceService;

    @Test
    void shouldAddDeviceSuccessfully() throws Exception {

        AddDeviceRequest addDeviceRequest = new AddDeviceRequest();
        addDeviceRequest.setBrandType(BrandType.GOOGLE);
        addDeviceRequest.setImei("123456789123456");
        addDeviceRequest.setModel("DeviceModel");
        addDeviceRequest.setPrice(new Money(100000, Currency.getInstance("USD")));
        addDeviceRequest.setFinancingEligible(true);

        String json = objectMapper.writeValueAsString(addDeviceRequest);

        DeviceAddedResponse response = new DeviceAddedResponse(
                1L,
                "DeviceModel",
                BrandType.GOOGLE,
                "123456789123456",
                new Money(100000, Currency.getInstance("USD")),
                true,
                true
        );

        when(deviceService.addDevice(any(AddDeviceRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/devices")
                .with(user("testuser"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("DeviceModel"))
                .andExpect(jsonPath("$.brand").value("GOOGLE"))
                .andExpect(jsonPath("$.price.amountInCents").value(100000))
                .andExpect(jsonPath("$.price.currency").value("USD"))
                .andExpect(jsonPath("$.imei").value("123456789123456"));

        verify(deviceService).addDevice(any(AddDeviceRequest.class));
    }
}
