package com.hrm.hrmauto.dto;

import java.math.BigDecimal;

public class PayrollResponse {

    private Long id;

    private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private String department;

    private String designation;

    private Integer year;

    private Integer month;

    private String structureName;

    // Earnings

    private BigDecimal basicSalary;

    private BigDecimal hra;

    private BigDecimal specialAllowance;

    private BigDecimal grossSalary;

    // Deductions

    private BigDecimal pfAmount;

    private BigDecimal esiAmount;

    private BigDecimal professionalTax;

    private BigDecimal totalDeductions;

    private BigDecimal netSalary;

    // Leave

    private Integer casualLeave;

    private Integer sickLeave;

    private Integer earnedLeave;

    private Integer casualLeaveUsed;

    private Integer sickLeaveUsed;

    private Integer earnedLeaveUsed;

    private String status;

    public PayrollResponse() {
    }

    public PayrollResponse(
            Long id,
            Long employeeId,
            String employeeCode,
            String employeeName,
            String department,
            String designation,
            Integer year,
            Integer month,
            String structureName,
            BigDecimal basicSalary,
            BigDecimal hra,
            BigDecimal specialAllowance,
            BigDecimal grossSalary,
            BigDecimal pfAmount,
            BigDecimal esiAmount,
            BigDecimal professionalTax,
            BigDecimal totalDeductions,
            BigDecimal netSalary,
            Integer casualLeave,
            Integer sickLeave,
            Integer earnedLeave,
            Integer casualLeaveUsed,
            Integer sickLeaveUsed,
            Integer earnedLeaveUsed,
            String status) {

        this.id = id;
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.department = department;
        this.designation = designation;
        this.year = year;
        this.month = month;
        this.structureName = structureName;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.specialAllowance = specialAllowance;
        this.grossSalary = grossSalary;
        this.pfAmount = pfAmount;
        this.esiAmount = esiAmount;
        this.professionalTax = professionalTax;
        this.totalDeductions = totalDeductions;
        this.netSalary = netSalary;
        this.casualLeave = casualLeave;
        this.sickLeave = sickLeave;
        this.earnedLeave = earnedLeave;
        this.casualLeaveUsed = casualLeaveUsed;
        this.sickLeaveUsed = sickLeaveUsed;
        this.earnedLeaveUsed = earnedLeaveUsed;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public String getStructureName() {
        return structureName;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public BigDecimal getHra() {
        return hra;
    }

    public BigDecimal getSpecialAllowance() {
        return specialAllowance;
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public BigDecimal getPfAmount() {
        return pfAmount;
    }

    public BigDecimal getEsiAmount() {
        return esiAmount;
    }

    public BigDecimal getProfessionalTax() {
        return professionalTax;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public Integer getCasualLeave() {
        return casualLeave;
    }

    public Integer getSickLeave() {
        return sickLeave;
    }

    public Integer getEarnedLeave() {
        return earnedLeave;
    }

    public Integer getCasualLeaveUsed() {
        return casualLeaveUsed;
    }

    public Integer getSickLeaveUsed() {
        return sickLeaveUsed;
    }

    public Integer getEarnedLeaveUsed() {
        return earnedLeaveUsed;
    }

    public String getStatus() {
        return status;
    }
}