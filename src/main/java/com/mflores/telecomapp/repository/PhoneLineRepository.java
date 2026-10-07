package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.PhoneLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneLineRepository extends JpaRepository<PhoneLine, Long> {

}
