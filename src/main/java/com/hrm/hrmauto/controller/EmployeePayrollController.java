package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.PayrollResponse;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.Payroll;
import com.hrm.hrmauto.repository.EmployeeRepository;
import com.hrm.hrmauto.repository.PayrollRepository;
import com.hrm.hrmauto.service.PayslipPdfService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/payroll")
public class EmployeePayrollController {
	private final PayslipPdfService payslipPdfService;
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeePayrollController(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            PayslipPdfService payslipPdfService) {

        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.payslipPdfService = payslipPdfService;
    }
    @GetMapping("/my/{year}/{month}/pdf")
    public ResponseEntity<byte[]> downloadMyPayslip(
            Authentication authentication,
            @PathVariable Integer year,
            @PathVariable Integer month) {

        String email = authentication.getName();

        Employee employee =
                employeeRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                )
                        );

        Payroll payroll =
                payrollRepository
                        .findByEmployeeIdAndPayYearAndPayMonth(
                                employee.getId(),
                                year,
                                month
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payroll not found for "
                                                + month
                                                + "/"
                                                + year
                                )
                        );

        try {

            byte[] pdf =
                    payslipPdfService.generatePayslipPdf(
                            payroll.getId()
                    );

            String filename =
                    "Payslip-"
                            + employee.getEmployeeCode()
                            + "-"
                            + month
                            + "-"
                            + year
                            + ".pdf";

            return ResponseEntity.ok()
                    .header(
                            "Content-Disposition",
                            "attachment; filename=\"" +
                                    filename +
                                    "\""
                    )
                    .header(
                            "Content-Type",
                            "application/pdf"
                    )
                    .body(pdf);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate payslip PDF",
                    e
            );
        }
    }
    // =====================================================
    // GET MY PAYROLL HISTORY
    // =====================================================

    @GetMapping("/my")
    public ResponseEntity<List<PayrollResponse>> getMyPayroll(
            Authentication authentication) {

        String email = authentication.getName();

        Employee employee =
                employeeRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with email "
                                                + email
                                )
                        );

        List<PayrollResponse> response =
                payrollRepository
                        .findByEmployeeIdOrderByPayYearDescPayMonthDesc(
                                employee.getId()
                        )
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // GET MY PAYROLL FOR SPECIFIC MONTH
    // =====================================================

    @GetMapping("/my/{year}/{month}")
    public ResponseEntity<PayrollResponse> getMyPayrollForMonth(
            Authentication authentication,
            @PathVariable Integer year,
            @PathVariable Integer month) {

        String email = authentication.getName();

        Employee employee =
                employeeRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with email "
                                                + email
                                )
                        );

        Payroll payroll =
                payrollRepository
                        .findByEmployeeIdAndPayYearAndPayMonth(
                                employee.getId(),
                                year,
                                month
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payroll not found for "
                                                + month
                                                + "/"
                                                + year
                                )
                        );

        return ResponseEntity.ok(
                convertToResponse(payroll)
        );
    }

    // =====================================================
    // CONVERT PAYROLL TO RESPONSE
    // =====================================================

    private PayrollResponse convertToResponse(
            Payroll payroll) {

        Employee employee =
                payroll.getEmployee();

        String structureName = null;

        if (payroll.getSalaryStructure() != null) {
            structureName =
                    payroll.getSalaryStructure()
                            .getStructureName();
        }

        return new PayrollResponse(

                payroll.getId(),

                employee.getId(),

                employee.getEmployeeCode(),

                employee.getName(),

                employee.getDepartment(),

                employee.getDesignation(),

                payroll.getPayYear(),

                payroll.getPayMonth(),

                structureName,

                // Earnings

                payroll.getBasicSalary(),

                payroll.getHra(),

                payroll.getSpecialAllowance(),

                payroll.getGrossSalary(),

                // Deductions

                payroll.getPfAmount(),

                payroll.getEsiAmount(),

                payroll.getProfessionalTax(),

                payroll.getTotalDeductions(),

                payroll.getNetSalary(),

                // Leave

                payroll.getCasualLeave(),

                payroll.getSickLeave(),

                payroll.getEarnedLeave(),

                payroll.getCasualLeaveUsed(),

                payroll.getSickLeaveUsed(),

                payroll.getEarnedLeaveUsed(),

                payroll.getStatus() != null
                        ? payroll.getStatus().name()
                        : null
        );
    }
}