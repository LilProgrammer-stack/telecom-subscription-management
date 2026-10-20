package com.mflores.telecomapp.tests.device.service;

import com.mflores.telecomapp.dto.AddDeviceRequest;
import com.mflores.telecomapp.dto.DeviceAddedResponse;
import com.mflores.telecomapp.model.BrandType;
import com.mflores.telecomapp.model.Device;
import com.mflores.telecomapp.model.Money;
import com.mflores.telecomapp.repository.DeviceRepository;
import com.mflores.telecomapp.service.DeviceService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;
    @InjectMocks
    private DeviceService deviceService;

    @Test
    void shouldSetLockToTrueWhenADeviceIsAdded(){

        AddDeviceRequest  addDeviceRequest = new AddDeviceRequest();
        addDeviceRequest.setBrandType(BrandType.GOOGLE);
        addDeviceRequest.setImei("123456789123456");
        addDeviceRequest.setModel("DeviceModel");
        addDeviceRequest.setPrice(new Money(100000, Currency.getInstance("USD")));

        when(deviceRepository.save(any(Device.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeviceAddedResponse addedResponse = deviceService.addDevice(addDeviceRequest);

        assertEquals(true, addedResponse.getLocked());
        verify(deviceRepository).save(argThat(device ->
                device.getLocked()
        ));
    }

    @Test
    void shouldCreateDeviceUsingRequestData() {

        AddDeviceRequest request = new AddDeviceRequest();
        request.setBrandType(BrandType.GOOGLE);
        request.setImei("123456789123456");
        request.setModel("DeviceModel");

        Money price = new Money(100000, Currency.getInstance("USD"));
        request.setPrice(price);

        when(deviceRepository.save(any(Device.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeviceAddedResponse response = deviceService.addDevice(request);

        assertEquals("DeviceModel", response.getModel());
        assertEquals("123456789123456", response.getImei());
        assertEquals(BrandType.GOOGLE, response.getBrand());
        assertEquals(price, response.getPrice());
    }
}
