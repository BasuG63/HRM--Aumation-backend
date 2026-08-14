package com.hrm.hrmauto.service;

import com.hrm.hrmauto.dto.PayrollProcessRequest;
import com.hrm.hrmauto.dto.PayrollResponse;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.EmployeeSalary;
import com.hrm.hrmauto.entity.LeaveApplication;
import com.hrm.hrmauto.entity.LeaveBalance;
import com.hrm.hrmauto.entity.LeaveStatus;
import com.hrm.hrmauto.entity.LeaveType;
import com.hrm.hrmauto.entity.Payroll;
import com.hrm.hrmauto.entity.PayrollStatus;
import com.hrm.hrmauto.entity.SalaryStructure;

import com.hrm.hrmauto.repository.EmployeeRepository;
import com.hrm.hrmauto.repository.EmployeeSalaryRepository;
import com.hrm.hrmauto.repository.LeaveApplicationRepository;
import com.hrm.hrmauto.repository.LeaveBalanceRepository;
import com.hrm.hrmauto.repository.PayrollRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeSalaryRepository employeeSalaryRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;

    public PayrollService(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository,
            EmployeeSalaryRepository employeeSalaryRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            LeaveApplicationRepository leaveApplicationRepository) {

        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    // =====================================================
    // PROCESS PAYROLL
    // =====================================================

    @Transactional
    public PayrollResponse processPayroll(
            PayrollProcessRequest request) {

        // =================================================
        // 1. VALIDATION
        // =================================================

        if (request == null) {
            throw new RuntimeException(
                    "Payroll request is required"
            );
        }

        Long employeeId =
                request.getEmployeeId();

        Integer year =
                request.getYear();

        Integer month =
                request.getMonth();

        if (employeeId == null) {
            throw new RuntimeException(
                    "Employee ID is required"
            );
        }

        if (year == null) {
            throw new RuntimeException(
                    "Payroll year is required"
            );
        }

        if (month == null ||
                month < 1 ||
                month > 12) {

            throw new RuntimeException(
                    "Payroll month must be between 1 and 12"
            );
        }

        // =================================================
        // 2. FIND EMPLOYEE
        // =================================================

        Employee employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found: "
                                                + employeeId
                                )
                        );

        // =================================================
        // 3. DUPLICATE PAYROLL CHECK
        // =================================================

        if (payrollRepository
                .existsByEmployeeIdAndPayYearAndPayMonth(
                        employeeId,
                        year,
                        month)) {

            throw new RuntimeException(
                    "Payroll already processed for employee "
                            + employee.getEmployeeCode()
                            + " for "
                            + month
                            + "/"
                            + year
            );
        }

        // =================================================
        // 4. PAY DATE
        // =================================================

        LocalDate payDate =
                LocalDate.of(
                        year,
                        month,
                        1
                );

        // =================================================
        // 5. GET EMPLOYEE SALARY HISTORY
        // =================================================

     // =====================================================
     // GET SALARY VALID FOR PAY DATE
     // =====================================================

     EmployeeSalary employeeSalary =
             employeeSalaryRepository
                     .findSalaryForDate(
                             employeeId,
                             payDate
                     )
                     .stream()
                     .findFirst()
                     .orElseThrow(() ->
                             new RuntimeException(
                                     "No salary found for employee "
                                             + employee.getEmployeeCode()
                                             + " for "
                                             + month
                                             + "/"
                                             + year
                             )
                     );

        // =================================================
        // 6. GET SALARY STRUCTURE
        // =================================================

        SalaryStructure salaryStructure =
                employeeSalary.getSalaryStructure();

        if (salaryStructure == null) {

            throw new RuntimeException(
                    "Salary structure not assigned for employee "
                            + employee.getEmployeeCode()
            );
        }

        // =================================================
        // 7. CREATE PAYROLL
        // =================================================

        Payroll payroll =
                new Payroll();

        payroll.setEmployee(employee);

        payroll.setPayYear(year);

        payroll.setPayMonth(month);

        // IMPORTANT:
        // Payroll expects SalaryStructure
        payroll.setSalaryStructure(
                salaryStructure
        );

        // =================================================
        // 8. EARNINGS
        // =================================================

        BigDecimal basic =
                salaryStructure.getBasicSalary();

        BigDecimal hra =
                salaryStructure.getHra();

        BigDecimal specialAllowance =
                salaryStructure.getSpecialAllowance();

        BigDecimal gross =
                basic
                        .add(hra)
                        .add(specialAllowance);

        payroll.setBasicSalary(basic);

        payroll.setHra(hra);

        payroll.setSpecialAllowance(
                specialAllowance
        );

        payroll.setGrossSalary(gross);

        // =================================================
        // 9. DEDUCTIONS
        // =================================================

        BigDecimal pf =
                salaryStructure.getPfAmount();

        BigDecimal esi =
                salaryStructure.getEsiAmount();

        BigDecimal professionalTax =
                salaryStructure.getProfessionalTax();

        BigDecimal totalDeductions =
                pf
                        .add(esi)
                        .add(professionalTax);

        BigDecimal netSalary =
                gross.subtract(
                        totalDeductions
                );

        payroll.setPfAmount(pf);

        payroll.setEsiAmount(esi);

        payroll.setProfessionalTax(
                professionalTax
        );

        payroll.setTotalDeductions(
                totalDeductions
        );

        payroll.setNetSalary(
                netSalary
        );

        // =================================================
        // 10. LEAVE BALANCE
        // =================================================

        LeaveBalance leaveBalance =
                leaveBalanceRepository
                        .findByEmployeeIdAndYear(
                                employeeId,
                                year
                        )
                        .orElse(null);

        if (leaveBalance != null) {

            payroll.setCasualLeave(
                    leaveBalance.getCasualLeave()
            );

            payroll.setSickLeave(
                    leaveBalance.getSickLeave()
            );

            payroll.setEarnedLeave(
                    leaveBalance.getEarnedLeave()
            );

        } else {

            payroll.setCasualLeave(0);

            payroll.setSickLeave(0);

            payroll.setEarnedLeave(0);
        }

        // =================================================
        // 11. MONTH DATE RANGE
        // =================================================

        LocalDate monthStart =
                LocalDate.of(
                        year,
                        month,
                        1
                );

        LocalDate monthEnd =
                monthStart.withDayOfMonth(
                        monthStart.lengthOfMonth()
                );

        // =================================================
        // 12. GET APPROVED LEAVES
        // =================================================

        List<LeaveApplication> approvedLeaves =
                leaveApplicationRepository
                        .findByEmployeeIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                employeeId,
                                LeaveStatus.APPROVED,
                                monthEnd,
                                monthStart
                        );

        // =================================================
        // 13. CALCULATE LEAVE USED
        // =================================================

        int clUsed = 0;
        int slUsed = 0;
        int elUsed = 0;

        for (LeaveApplication leave :
                approvedLeaves) {

            int days =
                    calculateOverlappingDays(
                            leave.getStartDate(),
                            leave.getEndDate(),
                            monthStart,
                            monthEnd
                    );

            if (leave.getLeaveType()
                    == LeaveType.CL) {

                clUsed += days;

            } else if (
                    leave.getLeaveType()
                            == LeaveType.SL) {

                slUsed += days;

            } else if (
                    leave.getLeaveType()
                            == LeaveType.EL) {

                elUsed += days;
            }
        }

        // =================================================
        // 14. SET LEAVE USED
        // =================================================

        payroll.setCasualLeaveUsed(
                clUsed
        );

        payroll.setSickLeaveUsed(
                slUsed
        );

        payroll.setEarnedLeaveUsed(
                elUsed
        );

        // =================================================
        // 15. PAYROLL STATUS
        // =================================================

        payroll.setStatus(
                PayrollStatus.PROCESSED
        );

        payroll.setProcessedAt(
                LocalDateTime.now()
        );

        // =================================================
        // 16. EMAIL STATUS
        // =================================================

        payroll.setEmailStatus(
                "PENDING"
        );

        // =================================================
        // 17. SAVE PAYROLL
        // =================================================

        Payroll savedPayroll =
                payrollRepository.save(
                        payroll
                );

        // =================================================
        // 18. RESPONSE
        // =================================================

        return convertToResponse(
                savedPayroll
        );
    }

    // =====================================================
    // CALCULATE OVERLAPPING DAYS
    // =====================================================

    private int calculateOverlappingDays(
            LocalDate leaveStart,
            LocalDate leaveEnd,
            LocalDate monthStart,
            LocalDate monthEnd) {

        LocalDate start =
                leaveStart.isAfter(monthStart)
                        ? leaveStart
                        : monthStart;

        LocalDate end =
                leaveEnd.isBefore(monthEnd)
                        ? leaveEnd
                        : monthEnd;

        if (start.isAfter(end)) {
            return 0;
        }

        return (int)
                (ChronoUnit.DAYS.between(
                        start,
                        end
                ) + 1);
    }

    // =====================================================
    // GET PAYROLL
    // =====================================================

    public PayrollResponse getPayroll(
            Long payrollId) {

        Payroll payroll =
                payrollRepository
                        .findById(payrollId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payroll not found: "
                                                + payrollId
                                )
                        );

        return convertToResponse(
                payroll
        );
    }

    // =====================================================
    // EMPLOYEE PAYROLL HISTORY
    // =====================================================

    public List<PayrollResponse>
    getEmployeePayrollHistory(
            Long employeeId) {

        return payrollRepository
                .findByEmployeeIdOrderByPayYearDescPayMonthDesc(
                        employeeId
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // MONTH PAYROLL
    // =====================================================

    public List<PayrollResponse>
    getMonthlyPayroll(
            Integer year,
            Integer month) {

        return payrollRepository
                .findByPayYearAndPayMonth(
                        year,
                        month
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // CONVERT RESPONSE
    // =====================================================

    private PayrollResponse convertToResponse(
            Payroll payroll) {

        Employee employee =
                payroll.getEmployee();

        SalaryStructure salaryStructure =
                payroll.getSalaryStructure();

        return new PayrollResponse(

                payroll.getId(),

                employee.getId(),

                employee.getEmployeeCode(),

                employee.getName(),

                employee.getDepartment(),

                employee.getDesignation(),

                payroll.getPayYear(),

                payroll.getPayMonth(),

                salaryStructure != null
                        ? salaryStructure.getStructureName()
                        : null,

                payroll.getBasicSalary(),

                payroll.getHra(),

                payroll.getSpecialAllowance(),

                payroll.getGrossSalary(),

                payroll.getPfAmount(),

                payroll.getEsiAmount(),

                payroll.getProfessionalTax(),

                payroll.getTotalDeductions(),

                payroll.getNetSalary(),

                payroll.getCasualLeave(),

                payroll.getSickLeave(),

                payroll.getEarnedLeave(),

                payroll.getCasualLeaveUsed(),

                payroll.getSickLeaveUsed(),

                payroll.getEarnedLeaveUsed(),

                payroll.getStatus().name()
        );
    }
}