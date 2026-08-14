package com.hrm.hrmauto.dto;

public class LeaveBalanceResponse {

    private Long employeeId;

    private String employeeCode;

    private String employeeName;

    private Integer year;

    private Integer casualLeave;

    private Integer sickLeave;

    private Integer earnedLeave;

    public LeaveBalanceResponse(
            Long employeeId,
            String employeeCode,
            String employeeName,
            Integer year,
            Integer casualLeave,
            Integer sickLeave,
            Integer earnedLeave) {

        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.year = year;
        this.casualLeave = casualLeave;
        this.sickLeave = sickLeave;
        this.earnedLeave = earnedLeave;
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

    public Integer getYear() {
        return year;
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
}