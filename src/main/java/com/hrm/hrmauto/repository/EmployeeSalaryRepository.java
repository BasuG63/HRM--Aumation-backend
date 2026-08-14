package com.hrm.hrmauto.repository;

import com.hrm.hrmauto.entity.EmployeeSalary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeSalaryRepository
        extends JpaRepository<EmployeeSalary, Long> {

    // =====================================================
    // EMPLOYEE SALARY HISTORY
    // =====================================================

    List<EmployeeSalary>
    findByEmployeeIdOrderByEffectiveFromDesc(
            Long employeeId
    );

    // =====================================================
    // CURRENT ACTIVE SALARY
    // =====================================================

    Optional<EmployeeSalary>
    findFirstByEmployeeIdAndActiveTrueOrderByEffectiveFromDesc(
            Long employeeId
    );

    // =====================================================
    // SALARY VALID ON SPECIFIC DATE
    // Handles effectiveTo IS NULL
    // =====================================================

    @Query("""
        SELECT es
        FROM EmployeeSalary es
        WHERE es.employee.id = :employeeId
          AND es.effectiveFrom <= :payDate
          AND (
                es.effectiveTo IS NULL
                OR es.effectiveTo >= :payDate
              )
        ORDER BY es.effectiveFrom DESC
    """)
    List<EmployeeSalary> findSalaryForDate(
            @Param("employeeId") Long employeeId,
            @Param("payDate") LocalDate payDate
    );
}