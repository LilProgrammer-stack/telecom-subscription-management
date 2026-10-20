package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.DeviceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeviceAssignmentRepository extends JpaRepository<DeviceAssignment, Long> {

    List<DeviceAssignment> findByEndDateIsNull();
    List<DeviceAssignment> findByEndDateIsNotNull();
}
