package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.AccountOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOwnerRepository extends JpaRepository<AccountOwner, Long> {
}
