package com.hrm.hrmauto.repository;

import com.hrm.hrmauto.entity.Payroll;
import com.hrm.hrmauto.entity.PayrollStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository
        extends JpaRepository<Payroll, Long> {

	long countByStatus(PayrollStatus status);
    Optional<Payroll> findByEmployeeIdAndPayYearAndPayMonth(
            Long employeeId,
            Integer payYear,
            Integer payMonth
    );
    

    boolean existsByEmployeeIdAndPayYearAndPayMonth(
            Long employeeId,
            Integer payYear,
            Integer payMonth
    );

    List<Payroll> findByPayYearAndPayMonth(
            Integer payYear,
            Integer payMonth
    );

    List<Payroll> findByStatus(
            PayrollStatus status
    );

    List<Payroll> findByEmployeeIdOrderByPayYearDescPayMonthDesc(
            Long employeeId
    );
}