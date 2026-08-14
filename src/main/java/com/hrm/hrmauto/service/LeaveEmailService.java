package com.hrm.hrmauto.service;

import com.hrm.hrmauto.entity.LeaveApplication;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class LeaveEmailService {

    private final JavaMailSender mailSender;

    public LeaveEmailService(
            JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    // ==========================================
    // LEAVE APPROVED EMAIL
    // ==========================================

    public void sendLeaveApprovedEmail(
            LeaveApplication application) {

        String employeeEmail =
                application
                        .getEmployee()
                        .getEmail();

        String employeeName =
                application
                        .getEmployee()
                        .getName();

        String employeeCode =
                application
                        .getEmployee()
                        .getEmployeeCode();

        String leaveType =
                application
                        .getLeaveType()
                        .name();

        String startDate =
                application
                        .getStartDate()
                        .toString();

        String endDate =
                application
                        .getEndDate()
                        .toString();

        int numberOfDays =
                application
                        .getNumberOfDays();

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(employeeEmail);

        message.setSubject(
                "Leave Approved - "
                        + employeeCode
        );

        String body =
                "Dear "
                        + employeeName
                        + ",\n\n"

                        + "Your leave application has been "
                        + "approved by the HR department.\n\n"

                        + "==============================\n"
                        + "       LEAVE DETAILS\n"
                        + "==============================\n\n"

                        + "Employee ID : "
                        + employeeCode
                        + "\n"

                        + "Employee Name : "
                        + employeeName
                        + "\n"

                        + "Leave Type : "
                        + leaveType
                        + "\n"

                        + "Start Date : "
                        + startDate
                        + "\n"

                        + "End Date : "
                        + endDate
                        + "\n"

                        + "Number of Days : "
                        + numberOfDays
                        + "\n"

                        + "Reason : "
                        + application.getReason()
                        + "\n"

                        + "Status : APPROVED"
                        + "\n\n"

                        + "Your leave balance has been "
                        + "updated automatically.\n\n"

                        + "Regards,\n"
                        + "HR Department\n"
                        + "HRM Company";

        message.setText(body);

        mailSender.send(message);
    }

    // ==========================================
    // LEAVE REJECTED EMAIL
    // ==========================================

    public void sendLeaveRejectedEmail(
            LeaveApplication application) {

        String employeeEmail =
                application
                        .getEmployee()
                        .getEmail();

        String employeeName =
                application
                        .getEmployee()
                        .getName();

        String employeeCode =
                application
                        .getEmployee()
                        .getEmployeeCode();

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(employeeEmail);

        message.setSubject(
                "Leave Rejected - "
                        + employeeCode
        );

        String body =
                "Dear "
                        + employeeName
                        + ",\n\n"

                        + "Your leave application has been "
                        + "reviewed by the HR department.\n\n"

                        + "==============================\n"
                        + "       LEAVE DETAILS\n"
                        + "==============================\n\n"

                        + "Employee ID : "
                        + employeeCode
                        + "\n"

                        + "Leave Type : "
                        + application
                        .getLeaveType()
                        .name()
                        + "\n"

                        + "Start Date : "
                        + application
                        .getStartDate()
                        + "\n"

                        + "End Date : "
                        + application
                        .getEndDate()
                        + "\n"

                        + "Number of Days : "
                        + application
                        .getNumberOfDays()
                        + "\n"

                        + "Reason : "
                        + application
                        .getReason()
                        + "\n"

                        + "Status : REJECTED"
                        + "\n\n"

                        + "Your leave balance has not been "
                        + "deducted.\n\n"

                        + "Regards,\n"
                        + "HR Department\n"
                        + "HRM Company";

        message.setText(body);

        mailSender.send(message);
    }
}