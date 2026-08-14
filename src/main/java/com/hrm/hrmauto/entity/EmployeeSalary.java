package com.hrm.hrmauto.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "employee_salary_history",
        indexes = {
                @Index(
                        name = "idx_employee_salary_employee",
                        columnList = "employee_id"
                ),
                @Index(
                        name = "idx_employee_salary_effective",
                        columnList = "employee_id,effective_from,effective_to"
                )
        }
)
public class EmployeeSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // EMPLOYEE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    private Employee employee;

    // =====================================================
    // SALARY STRUCTURE
    //
    // EmployeeSalary keeps a reference to the master
    // SalaryStructure used when salary was assigned.
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "salary_structure_id",
            nullable = false
    )
    private SalaryStructure salaryStructure;

    // =====================================================
    // SALARY SNAPSHOT
    //
    // These values are intentionally stored separately.
    // If the master salary structure changes later,
    // old salary history remains unchanged.
    // =====================================================

    @Column(
            name = "basic_salary",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal basicSalary;

    @Column(
            name = "hra",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal hra;

    @Column(
            name = "special_allowance",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal specialAllowance;

    @Column(
            name = "gross_salary",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal grossSalary;

    // =====================================================
    // DEDUCTIONS
    // =====================================================

    @Column(
            name = "pf_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal pfAmount;

    @Column(
            name = "esi_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal esiAmount;

    @Column(
            name = "professional_tax",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal professionalTax;

    // =====================================================
    // SALARY HISTORY DATES
    // =====================================================

    @Column(
            name = "effective_from",
            nullable = false
    )
    private LocalDate effectiveFrom;

    @Column(
            name = "effective_to"
    )
    private LocalDate effectiveTo;

    // =====================================================
    // ACTIVE
    // =====================================================

    @Column(
            nullable = false
    )
    private boolean active = true;

    // =====================================================
    // ASSIGNED DATE
    // =====================================================

    @Column(
            name = "assigned_at",
            nullable = false
    )
    private LocalDateTime assignedAt;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmployeeSalary() {
    }

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void onCreate() {

        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }

        if (effectiveFrom == null) {
            effectiveFrom = LocalDate.now();
        }

        if (active && effectiveTo != null) {
            active = false;
        }
    }

    // =====================================================
    // ID
    // =====================================================

    public Long getId() {
        return id;
    }

    // =====================================================
    // EMPLOYEE
    // =====================================================

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    // =====================================================
    // SALARY STRUCTURE
    // =====================================================

    public SalaryStructure getSalaryStructure() {
        return salaryStructure;
    }

    public void setSalaryStructure(
            SalaryStructure salaryStructure) {

        this.salaryStructure = salaryStructure;
    }

    // =====================================================
    // BASIC SALARY
    // =====================================================

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(
            BigDecimal basicSalary) {

        this.basicSalary = basicSalary;
    }

    // =====================================================
    // HRA
    // =====================================================

    public BigDecimal getHra() {
        return hra;
    }

    public void setHra(BigDecimal hra) {
        this.hra = hra;
    }

    // =====================================================
    // SPECIAL ALLOWANCE
    // =====================================================

    public BigDecimal getSpecialAllowance() {
        return specialAllowance;
    }

    public void setSpecialAllowance(
            BigDecimal specialAllowance) {

        this.specialAllowance = specialAllowance;
    }

    // =====================================================
    // GROSS SALARY
    // =====================================================

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(
            BigDecimal grossSalary) {

        this.grossSalary = grossSalary;
    }

    // =====================================================
    // PF
    // =====================================================

    public BigDecimal getPfAmount() {
        return pfAmount;
    }

    public void setPfAmount(
            BigDecimal pfAmount) {

        this.pfAmount = pfAmount;
    }

    // =====================================================
    // ESI
    // =====================================================

    public BigDecimal getEsiAmount() {
        return esiAmount;
    }

    public void setEsiAmount(
            BigDecimal esiAmount) {

        this.esiAmount = esiAmount;
    }

    // =====================================================
    // PROFESSIONAL TAX
    // =====================================================

    public BigDecimal getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(
            BigDecimal professionalTax) {

        this.professionalTax = professionalTax;
    }

    // =====================================================
    // EFFECTIVE FROM
    // =====================================================

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(
            LocalDate effectiveFrom) {

        this.effectiveFrom = effectiveFrom;
    }

    // =====================================================
    // EFFECTIVE TO
    // =====================================================

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(
            LocalDate effectiveTo) {

        this.effectiveTo = effectiveTo;
    }

    // =====================================================
    // ACTIVE
    // =====================================================

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // =====================================================
    // ASSIGNED AT
    // =====================================================

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(
            LocalDateTime assignedAt) {

        this.assignedAt = assignedAt;
    }
}