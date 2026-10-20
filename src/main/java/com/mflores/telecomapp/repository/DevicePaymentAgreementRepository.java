package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.DevicePaymentAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevicePaymentAgreementRepository extends JpaRepository<DevicePaymentAgreement, Long> {

    List<DevicePaymentAgreement> findByAccount(Account account);
}
