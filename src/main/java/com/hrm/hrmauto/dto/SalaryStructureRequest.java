package com.hrm.hrmauto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalaryStructureRequest {

    private String structureName;

    private BigDecimal basicSalary;

    private BigDecimal hra;

    private BigDecimal specialAllowance;

    private BigDecimal pfAmount;

    private BigDecimal esiAmount;

    private BigDecimal professionalTax;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    public SalaryStructureRequest() {
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getHra() {
        return hra;
    }

    public void setHra(BigDecimal hra) {
        this.hra = hra;
    }

    public BigDecimal getSpecialAllowance() {
        return specialAllowance;
    }

    public void setSpecialAllowance(
            BigDecimal specialAllowance) {

        this.specialAllowance = specialAllowance;
    }

    public BigDecimal getPfAmount() {
        return pfAmount;
    }

    public void setPfAmount(BigDecimal pfAmount) {
        this.pfAmount = pfAmount;
    }

    public BigDecimal getEsiAmount() {
        return esiAmount;
    }

    public void setEsiAmount(BigDecimal esiAmount) {
        this.esiAmount = esiAmount;
    }

    public BigDecimal getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(
            BigDecimal professionalTax) {

        this.professionalTax = professionalTax;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(
            LocalDate effectiveFrom) {

        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(
            LocalDate effectiveTo) {

        this.effectiveTo = effectiveTo;
    }
}