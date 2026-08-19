package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.EmployeeRequest;
import com.hrm.hrmauto.dto.EmployeeResponse;
import com.hrm.hrmauto.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmployeeController(
            EmployeeService employeeService) {

        this.employeeService = employeeService;
    }


    // =====================================================
    // SEARCH EMPLOYEE BY EMPLOYEE CODE
    // =====================================================

    @GetMapping("/search")
    public ResponseEntity<EmployeeResponse> searchEmployee(

            @RequestParam(value = "employeeCode", required = false)
            String employeeCode,

            @RequestParam(value = "phone", required = false)
            String phone) {

        if (employeeCode != null && !employeeCode.isBlank()) {

            return ResponseEntity.ok(
                    employeeService.getEmployeeByEmployeeCode(
                            employeeCode
                    )
            );
        }

        if (phone != null && !phone.isBlank()) {

            return ResponseEntity.ok(
                    employeeService.getEmployeeByPhone(
                            phone
                    )
            );
        }

        return ResponseEntity.badRequest().build();
    }    // =====================================================
    // CREATE EMPLOYEE
    // =====================================================

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =====================================================
    // GET ALL EMPLOYEES
    // =====================================================

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        List<EmployeeResponse> employees =
                employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }


    // =====================================================
    // GET EMPLOYEE BY DATABASE ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        EmployeeResponse employee =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }
}