package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.PlanChange;
import com.mflores.telecomapp.model.PlanChangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface PlanChangeRepository extends JpaRepository<PlanChange, Long> {

    List<PlanChange> findByStatusAndEffectiveAtLessThanEqual(
            PlanChangeStatus status,
            OffsetDateTime effectiveAt
    );
}
