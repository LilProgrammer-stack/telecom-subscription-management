package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.BillingCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingCycleRepository extends JpaRepository<BillingCycle, Long> {

    Optional<BillingCycle> findFirstByAccountOrderByCycleNumberDesc(Account account);
}
