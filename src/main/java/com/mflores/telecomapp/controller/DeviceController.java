package com.mflores.telecomapp.controller;

import com.mflores.telecomapp.dto.AddDeviceRequest;
import com.mflores.telecomapp.dto.DeviceAddedResponse;
import com.mflores.telecomapp.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceAddedResponse> addDevice(@RequestBody @Valid AddDeviceRequest addDeviceRequest) {

        DeviceAddedResponse deviceAddedResponse = deviceService.addDevice(addDeviceRequest);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deviceAddedResponse.getDeviceId())
                .toUri();

        return ResponseEntity.created(uri).body(deviceAddedResponse);
    }
}
