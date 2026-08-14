package com.hrm.hrmauto.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payrolls",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_payroll_month",
                        columnNames = {
                                "employee_id",
                                "pay_month",
                                "pay_year"
                        }
                )
        }
)
public class Payroll {

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
    // IMPORTANT:
    // payrolls.salary_structure_id -> salary_structures.id
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "salary_structure_id",
            nullable = false
    )
    private SalaryStructure salaryStructure;

    // =====================================================
    // PAY PERIOD
    // =====================================================

    @Column(
            name = "pay_month",
            nullable = false
    )
    private Integer payMonth;

    @Column(
            name = "pay_year",
            nullable = false
    )
    private Integer payYear;

    private LocalDate payPeriodStart;

    private LocalDate payPeriodEnd;

    // =====================================================
    // EARNINGS
    // =====================================================

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal basicSalary;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal hra;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal specialAllowance;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal grossSalary;

    // =====================================================
    // DEDUCTIONS
    // =====================================================

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal pfAmount;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal esiAmount;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal professionalTax;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalDeductions;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal netSalary;

    // =====================================================
    // LEAVE BALANCE
    // =====================================================

    private Integer casualLeave;

    private Integer sickLeave;

    private Integer earnedLeave;

    // =====================================================
    // LEAVE USED
    // =====================================================

    private Integer casualLeaveUsed;

    private Integer sickLeaveUsed;

    private Integer earnedLeaveUsed;

    // =====================================================
    // PAYROLL PROCESSING
    // =====================================================

    private LocalDateTime processedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatus status;

    // =====================================================
    // EMAIL STATUS
    // =====================================================

    @Column(
            name = "email_status",
            length = 30
    )
    private String emailStatus;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Payroll() {
    }

    // =====================================================
    // PRE PERSIST
    // =====================================================

    @PrePersist
    public void onCreate() {

        if (processedAt == null) {
            processedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = PayrollStatus.PROCESSED;
        }

        if (emailStatus == null) {
            emailStatus = "PENDING";
        }
    }

    // =====================================================
    // GETTERS / SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

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
    // PAY PERIOD
    // =====================================================

    public Integer getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(Integer payMonth) {
        this.payMonth = payMonth;
    }

    public Integer getPayYear() {
        return payYear;
    }

    public void setPayYear(Integer payYear) {
        this.payYear = payYear;
    }

    public LocalDate getPayPeriodStart() {
        return payPeriodStart;
    }

    public void setPayPeriodStart(
            LocalDate payPeriodStart) {

        this.payPeriodStart = payPeriodStart;
    }

    public LocalDate getPayPeriodEnd() {
        return payPeriodEnd;
    }

    public void setPayPeriodEnd(
            LocalDate payPeriodEnd) {

        this.payPeriodEnd = payPeriodEnd;
    }

    // =====================================================
    // EARNINGS
    // =====================================================

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(
            BigDecimal basicSalary) {

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

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(
            BigDecimal grossSalary) {

        this.grossSalary = grossSalary;
    }

    // =====================================================
    // DEDUCTIONS
    // =====================================================

    public BigDecimal getPfAmount() {
        return pfAmount;
    }

    public void setPfAmount(
            BigDecimal pfAmount) {

        this.pfAmount = pfAmount;
    }

    public BigDecimal getEsiAmount() {
        return esiAmount;
    }

    public void setEsiAmount(
            BigDecimal esiAmount) {

        this.esiAmount = esiAmount;
    }

    public BigDecimal getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(
            BigDecimal professionalTax) {

        this.professionalTax = professionalTax;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(
            BigDecimal totalDeductions) {

        this.totalDeductions = totalDeductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(
            BigDecimal netSalary) {

        this.netSalary = netSalary;
    }

    // =====================================================
    // LEAVE BALANCE
    // =====================================================

    public Integer getCasualLeave() {
        return casualLeave;
    }

    public void setCasualLeave(
            Integer casualLeave) {

        this.casualLeave = casualLeave;
    }

    public Integer getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(
            Integer sickLeave) {

        this.sickLeave = sickLeave;
    }

    public Integer getEarnedLeave() {
        return earnedLeave;
    }

    public void setEarnedLeave(
            Integer earnedLeave) {

        this.earnedLeave = earnedLeave;
    }

    // =====================================================
    // LEAVE USED
    // =====================================================

    public Integer getCasualLeaveUsed() {
        return casualLeaveUsed;
    }

    public void setCasualLeaveUsed(
            Integer casualLeaveUsed) {

        this.casualLeaveUsed = casualLeaveUsed;
    }

    public Integer getSickLeaveUsed() {
        return sickLeaveUsed;
    }

    public void setSickLeaveUsed(
            Integer sickLeaveUsed) {

        this.sickLeaveUsed = sickLeaveUsed;
    }

    public Integer getEarnedLeaveUsed() {
        return earnedLeaveUsed;
    }

    public void setEarnedLeaveUsed(
            Integer earnedLeaveUsed) {

        this.earnedLeaveUsed = earnedLeaveUsed;
    }

    // =====================================================
    // PROCESSING
    // =====================================================

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(
            LocalDateTime processedAt) {

        this.processedAt = processedAt;
    }

    // =====================================================
    // STATUS
    // =====================================================

    public PayrollStatus getStatus() {
        return status;
    }

    public void setStatus(
            PayrollStatus status) {

        this.status = status;
    }

    // =====================================================
    // EMAIL STATUS
    // =====================================================

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(
            String emailStatus) {

        this.emailStatus = emailStatus;
    }
}