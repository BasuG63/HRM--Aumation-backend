package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.EmployeeSalaryRequest;
import com.hrm.hrmauto.dto.EmployeeSalaryResponse;
import com.hrm.hrmauto.service.EmployeeSalaryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/employee-salary")
public class EmployeeSalaryController {

    private final EmployeeSalaryService
            employeeSalaryService;

    public EmployeeSalaryController(
            EmployeeSalaryService employeeSalaryService) {

        this.employeeSalaryService =
                employeeSalaryService;
    }

    // =====================================================
    // ASSIGN SALARY
    // =====================================================

    @PostMapping("/assign")
    public ResponseEntity<EmployeeSalaryResponse>
    assignSalary(
            @RequestBody EmployeeSalaryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        employeeSalaryService
                                .assignSalary(request)
                );
    }

    // =====================================================
    // CURRENT SALARY
    // =====================================================

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeSalaryResponse>
    getCurrentSalary(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                employeeSalaryService
                        .getCurrentSalary(
                                employeeId
                        )
        );
    }

    // =====================================================
    // SALARY HISTORY
    // =====================================================

    @GetMapping("/{employeeId}/history")
    public ResponseEntity<
            List<EmployeeSalaryResponse>>
    getSalaryHistory(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                employeeSalaryService
                        .getSalaryHistory(
                                employeeId
                        )
        );
    }
}