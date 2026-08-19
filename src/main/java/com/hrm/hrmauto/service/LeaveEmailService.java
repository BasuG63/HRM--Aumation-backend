package com.hrm.hrmauto.service;

import com.hrm.hrmauto.entity.LeaveApplication;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class LeaveEmailService {

    private final JavaMailSender mailSender;

    public LeaveEmailService(
            JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    // =====================================================
    // LEAVE APPROVED EMAIL
    // =====================================================

    public void sendLeaveApprovedEmail(
            LeaveApplication application) {

        try {

            String employeeEmail =
                    application.getEmployee().getEmail();

            String employeeName =
                    application.getEmployee().getName();

            String employeeCode =
                    application.getEmployee().getEmployeeCode();

            String leaveType =
                    application.getLeaveType().name();

            String startDate =
                    application.getStartDate().toString();

            String endDate =
                    application.getEndDate().toString();

            int numberOfDays =
                    application.getNumberOfDays();

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "LEAVE APPROVAL EMAIL"
            );

            System.out.println(
                    "Employee: " + employeeName
            );

            System.out.println(
                    "Recipient: " + employeeEmail
            );

            System.out.println(
                    "Leave ID: " + application.getId()
            );

            // =================================================
            // CREATE MIME MESSAGE
            // =================================================

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            // =================================================
            // RECIPIENT
            // =================================================

            helper.setTo(employeeEmail);

            // =================================================
            // SUBJECT
            // =================================================

            helper.setSubject(
                    "Leave Approved - "
                            + employeeCode
            );

            // =================================================
            // EMAIL BODY
            // =================================================

            String html =
                    "<html>"
                    + "<body style='"
                    + "font-family:Arial,sans-serif;"
                    + "background:#f4f6f8;"
                    + "padding:20px;"
                    + "'>"

                    + "<div style='"
                    + "max-width:650px;"
                    + "margin:auto;"
                    + "background:white;"
                    + "border-radius:10px;"
                    + "padding:30px;"
                    + "border:1px solid #ddd;"
                    + "'>"

                    + "<h2 style='color:#1f4e79;'>"
                    + "Leave Approved"
                    + "</h2>"

                    + "<p>"
                    + "Dear <b>"
                    + safe(employeeName)
                    + "</b>,"
                    + "</p>"

                    + "<p>"
                    + "Your leave application has been "
                    + "approved by the HR department."
                    + "</p>"

                    + "<h3 style='color:#1f4e79;'>"
                    + "Leave Details"
                    + "</h3>"

                    + "<table style='"
                    + "border-collapse:collapse;"
                    + "width:100%;"
                    + "'>"

                    + row(
                            "Employee ID",
                            employeeCode
                    )

                    + row(
                            "Employee Name",
                            employeeName
                    )

                    + row(
                            "Leave Type",
                            leaveType
                    )

                    + row(
                            "Start Date",
                            startDate
                    )

                    + row(
                            "End Date",
                            endDate
                    )

                    + row(
                            "Number of Days",
                            String.valueOf(numberOfDays)
                    )

                    + row(
                            "Reason",
                            application.getReason()
                    )

                    + row(
                            "Status",
                            "APPROVED"
                    )

                    + "</table>"

                    + "<p style='"
                    + "background:#d4edda;"
                    + "padding:15px;"
                    + "border:1px solid #c3e6cb;"
                    + "'>"
                    + "<b>Your leave has been approved.</b>"
                    + "<br>"
                    + "Your leave balance has been "
                    + "updated automatically."
                    + "</p>"

                    + "<p>"
                    + "Regards,<br>"
                    + "<b>HR Department</b><br>"
                    + "Future Transformer Company"
                    + "</p>"

                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(
                    html,
                    true
            );

            // =================================================
            // SEND
            // =================================================

            System.out.println(
                    "SENDING LEAVE APPROVAL EMAIL TO: "
                            + employeeEmail
            );

            mailSender.send(message);

            System.out.println(
                    "LEAVE APPROVAL EMAIL SENT SUCCESSFULLY TO: "
                            + employeeEmail
            );

            System.out.println(
                    "========================================"
            );

        } catch (MessagingException e) {

            System.err.println(
                    "LEAVE APPROVAL EMAIL FAILED"
            );

            e.printStackTrace();

        } catch (Exception e) {

            System.err.println(
                    "LEAVE APPROVAL EMAIL PROCESS FAILED"
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // LEAVE REJECTED EMAIL
    // =====================================================

    public void sendLeaveRejectedEmail(
            LeaveApplication application) {

        try {

            String employeeEmail =
                    application.getEmployee().getEmail();

            String employeeName =
                    application.getEmployee().getName();

            String employeeCode =
                    application.getEmployee().getEmployeeCode();

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "LEAVE REJECTION EMAIL"
            );

            System.out.println(
                    "Employee: " + employeeName
            );

            System.out.println(
                    "Recipient: " + employeeEmail
            );

            // =================================================
            // CREATE MIME MESSAGE
            // =================================================

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            helper.setTo(employeeEmail);

            helper.setSubject(
                    "Leave Rejected - "
                            + employeeCode
            );

            String html =
                    "<html>"
                    + "<body style='"
                    + "font-family:Arial,sans-serif;"
                    + "background:#f4f6f8;"
                    + "padding:20px;"
                    + "'>"

                    + "<div style='"
                    + "max-width:650px;"
                    + "margin:auto;"
                    + "background:white;"
                    + "border-radius:10px;"
                    + "padding:30px;"
                    + "border:1px solid #ddd;"
                    + "'>"

                    + "<h2 style='color:#c0392b;'>"
                    + "Leave Rejected"
                    + "</h2>"

                    + "<p>"
                    + "Dear <b>"
                    + safe(employeeName)
                    + "</b>,"
                    + "</p>"

                    + "<p>"
                    + "Your leave application has been "
                    + "reviewed by the HR department."
                    + "</p>"

                    + "<h3>Leave Details</h3>"

                    + "<table style='"
                    + "border-collapse:collapse;"
                    + "width:100%;"
                    + "'>"

                    + row(
                            "Employee ID",
                            employeeCode
                    )

                    + row(
                            "Leave Type",
                            application
                                    .getLeaveType()
                                    .name()
                    )

                    + row(
                            "Start Date",
                            String.valueOf(
                                    application.getStartDate()
                            )
                    )

                    + row(
                            "End Date",
                            String.valueOf(
                                    application.getEndDate()
                            )
                    )

                    + row(
                            "Number of Days",
                            String.valueOf(
                                    application.getNumberOfDays()
                            )
                    )

                    + row(
                            "Reason",
                            application.getReason()
                    )

                    + row(
                            "Status",
                            "REJECTED"
                    )

                    + "</table>"

                    + "<p style='"
                    + "background:#f8d7da;"
                    + "padding:15px;"
                    + "border:1px solid #f5c6cb;"
                    + "'>"
                    + "<b>Your leave request has been rejected.</b>"
                    + "<br>"
                    + "Your leave balance has not been deducted."
                    + "</p>"

                    + "<p>"
                    + "Regards,<br>"
                    + "<b>HR Department</b><br>"
                    + "Future Transformer Company"
                    + "</p>"

                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(
                    html,
                    true
            );

            System.out.println(
                    "SENDING LEAVE REJECTION EMAIL TO: "
                            + employeeEmail
            );

            mailSender.send(message);

            System.out.println(
                    "LEAVE REJECTION EMAIL SENT SUCCESSFULLY TO: "
                            + employeeEmail
            );

            System.out.println(
                    "========================================"
            );

        } catch (MessagingException e) {

            System.err.println(
                    "LEAVE REJECTION EMAIL FAILED"
            );

            e.printStackTrace();

        } catch (Exception e) {

            System.err.println(
                    "LEAVE REJECTION EMAIL PROCESS FAILED"
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // HTML TABLE ROW
    // =====================================================

    private String row(
            String label,
            String value) {

        return "<tr>"

                + "<td style='"
                + "padding:10px;"
                + "border:1px solid #ddd;"
                + "font-weight:bold;"
                + "width:40%;"
                + "'>"
                + safe(label)
                + "</td>"

                + "<td style='"
                + "padding:10px;"
                + "border:1px solid #ddd;"
                + "'>"
                + safe(value)
                + "</td>"

                + "</tr>";
    }


    // =====================================================
    // NULL SAFE
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? "N/A"
                : value;
    }
}