package com.hrm.hrmauto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalaryStructureResponse {

    private Long id;

    private String structureName;

    private BigDecimal basicSalary;

    private BigDecimal hra;

    private BigDecimal specialAllowance;

    private BigDecimal grossSalary;

    private BigDecimal pfAmount;

    private BigDecimal esiAmount;

    private BigDecimal professionalTax;

    private BigDecimal totalDeductions;

    private BigDecimal estimatedNetSalary;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private boolean active;

    public SalaryStructureResponse(
            Long id,
            String structureName,
            BigDecimal basicSalary,
            BigDecimal hra,
            BigDecimal specialAllowance,
            BigDecimal grossSalary,
            BigDecimal pfAmount,
            BigDecimal esiAmount,
            BigDecimal professionalTax,
            BigDecimal totalDeductions,
            BigDecimal estimatedNetSalary,
            LocalDate effectiveFrom,
            LocalDate effectiveTo,
            boolean active) {

        this.id = id;
        this.structureName = structureName;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.specialAllowance = specialAllowance;
        this.grossSalary = grossSalary;
        this.pfAmount = pfAmount;
        this.esiAmount = esiAmount;
        this.professionalTax = professionalTax;
        this.totalDeductions = totalDeductions;
        this.estimatedNetSalary = estimatedNetSalary;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.active = active;
    }

    public Long getId() {
        return id;
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

    public BigDecimal getEstimatedNetSalary() {
        return estimatedNetSalary;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public boolean isActive() {
        return active;
    }
}