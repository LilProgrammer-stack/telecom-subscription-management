package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
