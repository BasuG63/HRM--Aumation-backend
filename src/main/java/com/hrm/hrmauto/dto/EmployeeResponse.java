package com.hrm.hrmauto.dto;

import java.time.LocalDate;

public class EmployeeResponse {

    private Long id;

    private String employeeCode;

    private String name;

    private String email;

    private String phone;

    private String department;

    private String designation;

    private LocalDate joiningDate;

    private String message;


    public EmployeeResponse() {
    }


    public EmployeeResponse(
            Long id,
            String employeeCode,
            String name,
            String email,
            String phone,
            String department,
            String designation,
            LocalDate joiningDate,
            String message) {

        this.id = id;
        this.employeeCode = employeeCode;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.message = message;
    }


    public Long getId() {
        return id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public String getMessage() {
        return message;
    }
}