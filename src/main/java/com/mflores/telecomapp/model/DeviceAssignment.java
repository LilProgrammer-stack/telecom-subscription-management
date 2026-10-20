package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "device_assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_assignment_id", nullable = false)
    private Long deviceAssignmentId;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column
    private LocalDate endDate;
    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne
    private Device device;
    @JoinColumn(name = "phone_line_id", nullable = false)
    @ManyToOne
    private PhoneLine phoneLine;
}
