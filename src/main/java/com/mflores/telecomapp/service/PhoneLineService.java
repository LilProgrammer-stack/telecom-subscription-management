package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.PhoneLineResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.PhoneLine;
import com.mflores.telecomapp.model.PhoneLineStatus;
import com.mflores.telecomapp.repository.PhoneLineRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhoneLineService {

    private final PhoneLineRepository phoneLineRepository;
    private final PhoneNumberGenerator phoneNumberGenerator;

    public PhoneLineService(PhoneLineRepository phoneLineRepository, PhoneNumberGenerator phoneNumberGenerator) {
        this.phoneLineRepository = phoneLineRepository;
        this.phoneNumberGenerator = phoneNumberGenerator;
    }

    public PhoneLineResponse createPhoneLine(Account account) {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setPhoneNumber(phoneNumberGenerator.generatePhoneNumber());

        PhoneLine savedPhoneLine = phoneLineRepository.save(phoneLine);

        return convertIntoPhoneLineResponse(savedPhoneLine);
    }

    public PhoneLine findPhoneLineEntityById(Long id) {

        return phoneLineRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Phone line not found with id: " + id));
    }

    public PhoneLineResponse suspendPhoneLine(PhoneLine phoneLine) {

        if (phoneLine.getPhoneLineStatus() == PhoneLineStatus.SUSPENDED) {
            throw new IllegalArgumentException("A suspended phone line cannot be suspended again. You can reactivate it instead.");
        } else if (phoneLine.getPhoneLineStatus() == PhoneLineStatus.TERMINATED) {
            throw new IllegalArgumentException("The phone number has been terminated, it cannot be reactivated or suspended");
        }

        phoneLine.setPhoneLineStatus(PhoneLineStatus.SUSPENDED);
        phoneLineRepository.save(phoneLine);
        return convertIntoPhoneLineResponse(phoneLine);

    }

    public PhoneLineResponse reactivatePhoneLine(PhoneLine phoneLine) {

        if (phoneLine.getPhoneLineStatus() == PhoneLineStatus.TERMINATED)  {
            throw new IllegalArgumentException("A terminated phone line cannot be reactivated");
        }else if (phoneLine.getPhoneLineStatus() == PhoneLineStatus.ACTIVE) {
            throw new IllegalArgumentException("The phone number has been activated already");
        }

        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLineRepository.save(phoneLine);
        return convertIntoPhoneLineResponse(phoneLine);
    }

    public PhoneLineResponse terminatePhoneLine(PhoneLine phoneLine) {

        if (phoneLine.getPhoneLineStatus() == PhoneLineStatus.TERMINATED) {
            throw new IllegalArgumentException("A terminated phone line cannot be terminated again");
        }

        phoneLine.setPhoneLineStatus(PhoneLineStatus.TERMINATED);
        phoneLineRepository.save(phoneLine);
        return  convertIntoPhoneLineResponse(phoneLine);
    }

    private PhoneLineResponse convertIntoPhoneLineResponse(PhoneLine phoneLine) {

        return new PhoneLineResponse(phoneLine.getPhoneLineId(), phoneLine.getPhoneNumber(),
                phoneLine.getPhoneLineStatus());
    }
}
