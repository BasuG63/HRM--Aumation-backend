package com.hrm.hrmauto.service;

import com.hrm.hrmauto.dto.EmployeeSalaryRequest;
import com.hrm.hrmauto.dto.EmployeeSalaryResponse;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.EmployeeSalary;
import com.hrm.hrmauto.entity.SalaryStructure;
import com.hrm.hrmauto.exception.BadRequestException;
import com.hrm.hrmauto.repository.EmployeeRepository;
import com.hrm.hrmauto.repository.EmployeeSalaryRepository;
import com.hrm.hrmauto.repository.SalaryStructureRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeSalaryService {

    private final EmployeeRepository employeeRepository;

    private final SalaryStructureRepository
            salaryStructureRepository;

    private final EmployeeSalaryRepository
            employeeSalaryRepository;

    public EmployeeSalaryService(
            EmployeeRepository employeeRepository,
            SalaryStructureRepository salaryStructureRepository,
            EmployeeSalaryRepository employeeSalaryRepository) {

        this.employeeRepository =
                employeeRepository;

        this.salaryStructureRepository =
                salaryStructureRepository;

        this.employeeSalaryRepository =
                employeeSalaryRepository;
    }

    // =====================================================
    // ASSIGN SALARY STRUCTURE TO EMPLOYEE
    // =====================================================

    @Transactional
    public EmployeeSalaryResponse assignSalary(
            EmployeeSalaryRequest request) {

        // =================================================
        // VALIDATION
        // =================================================

        if (request == null) {
            throw new RuntimeException(
                    "Salary request cannot be null"
            );
        }

        if (request.getEmployeeId() == null) {
            throw new RuntimeException(
                    "Employee ID is required"
            );
        }

        if (request.getSalaryStructureId() == null) {
            throw new RuntimeException(
                    "Salary structure ID is required"
            );
        }

        // =================================================
        // EFFECTIVE DATE
        // IMPORTANT:
        // Make it final so it can safely be used
        // inside lambda
        // =================================================

        final LocalDate effectiveFrom =
                request.getEffectiveFrom() != null
                        ? request.getEffectiveFrom()
                        : LocalDate.now();

        // =================================================
        // FIND EMPLOYEE
        // =================================================

        Employee employee =
                employeeRepository
                        .findById(
                                request.getEmployeeId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with id: "
                                                + request.getEmployeeId()
                                )
                        );

        // =================================================
        // FIND SALARY STRUCTURE
        // =================================================

        SalaryStructure structure =
                salaryStructureRepository
                        .findById(
                                request.getSalaryStructureId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary structure not found with id: "
                                                + request.getSalaryStructureId()
                                )
                        );

        // =================================================
        // CHECK ACTIVE STRUCTURE
        // =================================================

        if (!structure.isActive()) {

            throw new RuntimeException(
                    "Salary structure is not active"
            );
        }

        // =================================================
        // CLOSE CURRENT ACTIVE SALARY
        // =================================================
     // =====================================================
     // CLOSE CURRENT ACTIVE SALARY
     // =====================================================

     employeeSalaryRepository
             .findFirstByEmployeeIdAndActiveTrueOrderByEffectiveFromDesc(
                     employee.getId()
             )
             .ifPresent(currentSalary -> {

                 // New effective date must be after
                 // current salary effective date

                 if (!effectiveFrom.isAfter(
                         currentSalary.getEffectiveFrom()
                 )) {

                     throw new BadRequestException(
                             "New salary effective date must be after "
                             + "current salary effective date: "
                             + currentSalary.getEffectiveFrom()
                     );
                 }

                 // Close previous salary
                 currentSalary.setEffectiveTo(
                         effectiveFrom.minusDays(1)
                 );

                 currentSalary.setActive(false);

                 employeeSalaryRepository.save(
                         currentSalary
                 );
             });
        // =================================================
        // CREATE NEW SALARY HISTORY RECORD
        // =================================================

        EmployeeSalary salary =
                new EmployeeSalary();

        salary.setEmployee(employee);

        salary.setSalaryStructure(
                structure
        );

        // =================================================
        // SNAPSHOT SALARY VALUES
        // =================================================

        salary.setBasicSalary(
                structure.getBasicSalary()
        );

        salary.setHra(
                structure.getHra()
        );

        salary.setSpecialAllowance(
                structure.getSpecialAllowance()
        );

        salary.setGrossSalary(
                structure.getGrossSalary()
        );

        salary.setPfAmount(
                structure.getPfAmount()
        );

        salary.setEsiAmount(
                structure.getEsiAmount()
        );

        salary.setProfessionalTax(
                structure.getProfessionalTax()
        );

        // =================================================
        // HISTORY INFORMATION
        // =================================================

        salary.setEffectiveFrom(
                effectiveFrom
        );

        salary.setEffectiveTo(
                null
        );

        salary.setActive(true);

        // =================================================
        // SAVE
        // =================================================

        EmployeeSalary savedSalary =
                employeeSalaryRepository.save(
                        salary
                );

        // =================================================
        // RESPONSE
        // =================================================

        return convertToResponse(
                savedSalary
        );
    }

    // =====================================================
    // GET CURRENT SALARY
    // =====================================================

    public EmployeeSalaryResponse getCurrentSalary(
            Long employeeId) {

        EmployeeSalary salary =
                employeeSalaryRepository
                        .findFirstByEmployeeIdAndActiveTrueOrderByEffectiveFromDesc(
                                employeeId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No active salary found for employee "
                                                + employeeId
                                )
                        );

        return convertToResponse(
                salary
        );
    }

    // =====================================================
    // GET SALARY HISTORY
    // =====================================================

    public List<EmployeeSalaryResponse> getSalaryHistory(
            Long employeeId) {

        return employeeSalaryRepository
                .findByEmployeeIdOrderByEffectiveFromDesc(
                        employeeId
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // CONVERT ENTITY TO RESPONSE
    // =====================================================

    private EmployeeSalaryResponse convertToResponse(
            EmployeeSalary salary) {

        // =================================================
        // TOTAL DEDUCTIONS
        // =================================================

        BigDecimal totalDeductions =
                salary.getPfAmount()
                        .add(salary.getEsiAmount())
                        .add(salary.getProfessionalTax());

        // =================================================
        // NET SALARY
        // =================================================

        BigDecimal netSalary =
                salary.getGrossSalary()
                        .subtract(totalDeductions);

        // =================================================
        // STRUCTURE
        // =================================================

        SalaryStructure structure =
                salary.getSalaryStructure();

        Long structureId =
                structure != null
                        ? structure.getId()
                        : null;

        String structureName =
                structure != null
                        ? structure.getStructureName()
                        : null;

        // =================================================
        // RESPONSE
        // =================================================

        return new EmployeeSalaryResponse(

                salary.getId(),

                salary.getEmployee().getId(),

                salary.getEmployee().getEmployeeCode(),

                salary.getEmployee().getName(),

                structureId,

                structureName,

                salary.getBasicSalary(),

                salary.getHra(),

                salary.getSpecialAllowance(),

                salary.getGrossSalary(),

                salary.getPfAmount(),

                salary.getEsiAmount(),

                salary.getProfessionalTax(),

                totalDeductions,

                netSalary,

                salary.getEffectiveFrom(),

                salary.getEffectiveTo(),

                salary.isActive()
        );
    }
}