package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.CreateDevicePaymentAgreementRequest;
import com.mflores.telecomapp.dto.DevicePaymentAgreementResponse;
import com.mflores.telecomapp.exception.BusinessRuleViolationException;
import com.mflores.telecomapp.model.Device;
import com.mflores.telecomapp.model.DevicePaymentAgreement;
import com.mflores.telecomapp.repository.DevicePaymentAgreementRepository;
import com.mflores.telecomapp.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DevicePaymentAgreementService {

    private final DevicePaymentAgreementRepository devicePaymentAgreementRepository;
    private final DeviceService deviceService;

    public DevicePaymentAgreementService(DevicePaymentAgreementRepository devicePaymentAgreementRepository,
                                         DeviceService deviceService) {
        this.devicePaymentAgreementRepository = devicePaymentAgreementRepository;
        this.deviceService = deviceService;
    }

    @Transactional
    public DevicePaymentAgreementResponse createDevicePaymentAgreement(CreateDevicePaymentAgreementRequest createDevicePaymentAgreementRequest) {

        Device foundDevice = deviceService.findByDeviceEntityId(createDevicePaymentAgreementRequest.getDeviceId());

        if (foundDevice.getFinancingEligible().equals(false)) throw new BusinessRuleViolationException("The device is not eligible for financing");
        if (foundDevice.getPaymentAgreement() != null) throw new BusinessRuleViolationException("The device is already being financed");

        DevicePaymentAgreement dpa = new DevicePaymentAgreement();
        dpa.setFinancedAmount(foundDevice.getPrice());
        dpa.setStartDate(LocalDate.now());
        dpa.setEndDate(null);
        dpa.setTotalInstallments(36);
        dpa.setPaidInstallments(0);

        foundDevice.setPaymentAgreement(dpa);


        DevicePaymentAgreement savedDpa = devicePaymentAgreementRepository.save(dpa);


        return convertToDevicePaymentAgreementResponse(savedDpa);
    }

    private DevicePaymentAgreementResponse convertToDevicePaymentAgreementResponse(DevicePaymentAgreement dpa) {
        return new DevicePaymentAgreementResponse(dpa.getDevicePaymentAgreementId(), dpa.getStartDate(),
                dpa.getEndDate(), dpa.getFinancedAmount(), dpa.getTotalInstallments(), dpa.getPaidInstallments(),
                dpa.getAccount());
    }


}
