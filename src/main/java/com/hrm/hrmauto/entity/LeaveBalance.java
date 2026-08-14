package com.hrm.hrmauto.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "leave_balances",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_employee_year",
            columnNames = {"employee_id", "year"}
        )
    }
)
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "employee_id",
        nullable = false
    )
    private Employee employee;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "casual_leave", nullable = false)
    private Integer casualLeave;

    @Column(name = "sick_leave", nullable = false)
    private Integer sickLeave;

    @Column(name = "earned_leave", nullable = false)
    private Integer earnedLeave;

    public LeaveBalance() {
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getCasualLeave() {
        return casualLeave;
    }

    public void setCasualLeave(Integer casualLeave) {
        this.casualLeave = casualLeave;
    }

    public Integer getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(Integer sickLeave) {
        this.sickLeave = sickLeave;
    }

    public Integer getEarnedLeave() {
        return earnedLeave;
    }

    public void setEarnedLeave(Integer earnedLeave) {
        this.earnedLeave = earnedLeave;
    }
}