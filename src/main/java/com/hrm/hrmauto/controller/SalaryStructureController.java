package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.SalaryStructureRequest;
import com.hrm.hrmauto.dto.SalaryStructureResponse;
import com.hrm.hrmauto.service.SalaryStructureService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/salary-structures")
public class SalaryStructureController {

    private final SalaryStructureService salaryStructureService;

    public SalaryStructureController(
            SalaryStructureService salaryStructureService) {

        this.salaryStructureService =
                salaryStructureService;
    }

    // =====================================================
    // CREATE SALARY STRUCTURE
    // =====================================================

    @PostMapping
    public ResponseEntity<SalaryStructureResponse> create(
            @RequestBody SalaryStructureRequest request) {

        SalaryStructureResponse response =
                salaryStructureService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =====================================================
    // GET ALL SALARY STRUCTURES
    // =====================================================

    @GetMapping
    public ResponseEntity<List<SalaryStructureResponse>> getAll() {

        return ResponseEntity.ok(
                salaryStructureService.getAll()
        );
    }


    // =====================================================
    // GET ACTIVE SALARY STRUCTURES
    // =====================================================

    @GetMapping("/active")
    public ResponseEntity<List<SalaryStructureResponse>> getActive() {

        return ResponseEntity.ok(
                salaryStructureService.getActive()
        );
    }


    // =====================================================
    // GET SALARY STRUCTURE BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<SalaryStructureResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                salaryStructureService.getById(id)
        );
    }


    // =====================================================
    // UPDATE SALARY STRUCTURE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<SalaryStructureResponse> update(
            @PathVariable Long id,
            @RequestBody SalaryStructureRequest request) {

        return ResponseEntity.ok(
                salaryStructureService.update(
                        id,
                        request
                )
        );
    }


    // =====================================================
    // DELETE SALARY STRUCTURE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        salaryStructureService.delete(id);

        return ResponseEntity.noContent().build();
    }
}