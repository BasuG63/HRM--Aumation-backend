package com.hrm.hrmauto.repository;

import com.hrm.hrmauto.entity.LeaveApplication;
import com.hrm.hrmauto.entity.LeaveStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveApplicationRepository
        extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication>
    findByEmployeeIdOrderByAppliedAtDesc(
            Long employeeId
    );

    List<LeaveApplication>
    findByStatusOrderByAppliedAtDesc(
            LeaveStatus status
    );

    boolean
    existsByEmployeeIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long employeeId,
            List<LeaveStatus> statuses,
            LocalDate endDate,
            LocalDate startDate
    );

    List<LeaveApplication>
    findByEmployeeIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long employeeId,
            LeaveStatus status,
            LocalDate monthEnd,
            LocalDate monthStart
    );
}