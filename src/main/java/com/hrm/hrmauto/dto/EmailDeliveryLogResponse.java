package com.hrm.hrmauto.dto;

import java.time.LocalDateTime;

public class EmailDeliveryLogResponse {

    private Long id;
    private Long payrollId;
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String employeeEmail;
    private String status;
    private String errorMessage;
    private LocalDateTime sentAt;

    public EmailDeliveryLogResponse() {
    }

    public EmailDeliveryLogResponse(
            Long id,
            Long payrollId,
            Long employeeId,
            String employeeCode,
            String employeeName,
            String employeeEmail,
            String status,
            String errorMessage,
            LocalDateTime sentAt) {

        this.id = id;
        this.payrollId = payrollId;
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.status = status;
        this.errorMessage = errorMessage;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPayrollId() {
        return payrollId;
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

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}