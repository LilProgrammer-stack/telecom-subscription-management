package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.BillingCycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingCycleRepository extends JpaRepository<BillingCycle, Long> {
}
