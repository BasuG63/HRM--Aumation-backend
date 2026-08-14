package com.hrm.hrmauto.dto;

import java.time.LocalDate;

public class EmployeeSalaryRequest {

    private Long employeeId;

    private Long salaryStructureId;

    private LocalDate effectiveFrom;

    public EmployeeSalaryRequest() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId =
                employeeId;
    }

    public Long getSalaryStructureId() {
        return salaryStructureId;
    }

    public void setSalaryStructureId(
            Long salaryStructureId) {

        this.salaryStructureId =
                salaryStructureId;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(
            LocalDate effectiveFrom) {

        this.effectiveFrom =
                effectiveFrom;
    }
}