package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.PhoneLine;
import com.mflores.telecomapp.model.PlanAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanAssignmentRepository extends JpaRepository<PlanAssignment, Long> {

    Optional<PlanAssignment> findFirstByPhoneLineAndEndDateIsNull(PhoneLine phoneLine);

    @Query("""
        SELECT pa
        FROM PlanAssignment pa
        WHERE pa.startDate <= :periodEndDate
          AND (pa.endDate IS NULL OR pa.endDate >= :periodStartDate)
    """)
    List<PlanAssignment> findAssignmentsForBillingCycle(
            @Param("periodStartDate") LocalDate periodStartDate,
            @Param("periodEndDate") LocalDate periodEndDate
    );
}
