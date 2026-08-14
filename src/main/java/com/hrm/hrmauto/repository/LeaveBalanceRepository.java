package com.hrm.hrmauto.repository;

import com.hrm.hrmauto.entity.LeaveBalance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance>
    findByEmployeeIdAndYear(
            Long employeeId,
            int year
    );

    boolean existsByEmployeeIdAndYear(
            Long employeeId,
            int year
    );

    List<LeaveBalance> findByYear(
            int year
    );
}