package com.hrm.hrmauto.repository;

import com.hrm.hrmauto.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    // =====================================================
    // FIND EMPLOYEE BY EMAIL
    // Used by Leave Application
    // =====================================================

    Optional<Employee> findByEmail(String email);


    // =====================================================
    // FIND EMPLOYEE BY EMPLOYEE CODE
    // Used for HR employee search
    // =====================================================

    Optional<Employee> findByEmployeeCode(String employeeCode);


    // =====================================================
    // FIND EMPLOYEE BY PHONE
    // Used for HR employee search
    // =====================================================

    Optional<Employee> findByPhone(String phone);


    // =====================================================
    // CHECK DUPLICATE PHONE
    // Used while creating employee
    // =====================================================

    boolean existsByPhone(String phone);


    // =====================================================
    // CHECK DUPLICATE EMPLOYEE CODE
    // =====================================================

    boolean existsByEmployeeCode(String employeeCode);


    // =====================================================
    // CHECK DUPLICATE EMAIL
    // =====================================================

    boolean existsByEmail(String email);


    // =====================================================
    // GET HIGHEST FTC EMPLOYEE CODE NUMBER
    // Example: FTC16 -> 16
    // =====================================================

    @Query(
        value = """
            SELECT COALESCE(
                MAX(CAST(SUBSTRING(employee_code FROM 4) AS BIGINT)),
                11
            )
            FROM employees
            WHERE employee_code LIKE 'FTC%'
            """,
        nativeQuery = true
    )
    Long findMaxEmployeeCodeNumber();
}