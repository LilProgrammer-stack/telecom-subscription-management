package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.CustomerCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerCredentialRepository extends JpaRepository<CustomerCredential, Long> {

    Optional<CustomerCredential> findByAccountOwnerEmail(String email);

}
