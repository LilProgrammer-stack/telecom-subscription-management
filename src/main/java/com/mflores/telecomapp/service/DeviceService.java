package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.AddDeviceRequest;
import com.mflores.telecomapp.dto.DeviceAddedResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.Device;
import com.mflores.telecomapp.repository.DeviceRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceAddedResponse addDevice(AddDeviceRequest deviceRequest) {

        Device device = new Device();
        device.setBrand(deviceRequest.getBrandType());
        device.setModel(deviceRequest.getModel());
        device.setImei(deviceRequest.getImei());
        device.setPrice(deviceRequest.getPrice());
        device.setFinancingEligible(deviceRequest.getFinancingEligible());
        device.setLocked(true);

        Device savedDevice = deviceRepository.save(device);

        return convertToDeviceAddedResponse(savedDevice);
    }

    public Device findByDeviceEntityId(Long id){
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The device id: "+id+" was not found"));
    }

    private DeviceAddedResponse convertToDeviceAddedResponse(Device device) {
        return new DeviceAddedResponse(device.getDeviceId(),
                device.getModel(), device.getBrand(), device.getImei(), device.getPrice(), device.getLocked(),
                device.getFinancingEligible());
    }
}
