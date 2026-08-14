package com.hrm.hrmauto.dto;

import com.hrm.hrmauto.entity.LeaveStatus;
import com.hrm.hrmauto.entity.LeaveType;

import java.time.LocalDate;

public class LeaveApplicationResponse {

    private Long id;

    private String employeeCode;

    private String employeeName;

    private LeaveType leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer numberOfDays;

    private String reason;

    private LeaveStatus status;

    public LeaveApplicationResponse(
            Long id,
            String employeeCode,
            String employeeName,
            LeaveType leaveType,
            LocalDate startDate,
            LocalDate endDate,
            Integer numberOfDays,
            String reason,
            LeaveStatus status) {

        this.id = id;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public String getReason() {
        return reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }
}