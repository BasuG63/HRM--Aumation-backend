package com.hrm.hrmauto.service;

import com.hrm.hrmauto.entity.Employee;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmployeeEmailService {

    private final JavaMailSender mailSender;
    private final EmployeeIdCardPdfService pdfService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmployeeEmailService(
            JavaMailSender mailSender,
            EmployeeIdCardPdfService pdfService) {

        this.mailSender = mailSender;
        this.pdfService = pdfService;
    }

    // =====================================================
    // ASYNC EMPLOYEE WELCOME EMAIL
    // =====================================================

    @Async
    public void sendEmployeeWelcomeEmail(
            Employee employee,
            String temporaryPassword) {

        try {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "ASYNC EMAIL STARTED FOR: "
                            + employee.getEmail()
            );

            // =================================================
            // 1. GENERATE ID CARD PDF
            // =================================================

            byte[] pdf =
                    pdfService.generateIdCard(
                            employee
                    );

            System.out.println(
                    "ID CARD PDF GENERATED"
            );

            // =================================================
            // 2. CREATE EMAIL MESSAGE
            // =================================================

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            // =================================================
            // 3. RECIPIENT
            // =================================================

            helper.setTo(
                    employee.getEmail()
            );

            // =================================================
            // 4. SUBJECT
            // =================================================

            helper.setSubject(
                    "Welcome to Future Transformer Company - Employee ID "
                            + employee.getEmployeeCode()
            );

            // =================================================
            // 5. JOINING DATE
            // =================================================

            String joiningDate =
                    employee.getJoiningDate() != null
                            ? employee.getJoiningDate().toString()
                            : "N/A";

            // =================================================
            // 6. EMAIL HTML
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

                    // =================================================
                    // HEADER
                    // =================================================

                    + "<div style='"
                    + "background:#1f4e79;"
                    + "color:white;"
                    + "padding:20px;"
                    + "text-align:center;"
                    + "border-radius:8px;"
                    + "'>"

                    + "<h2 style='margin:0;'>"
                    + "Future Transformer Company"
                    + "</h2>"

                    + "<p style='margin:8px 0 0;'>"
                    + "Employee Welcome"
                    + "</p>"

                    + "</div>"

                    // =================================================
                    // GREETING
                    // =================================================

                    + "<p style='margin-top:25px;'>"
                    + "Dear <b>"
                    + safe(employee.getName())
                    + "</b>,"
                    + "</p>"

                    + "<p>"
                    + "Your employee account has been successfully "
                    + "created by the HR department."
                    + "</p>"

                    // =================================================
                    // EMPLOYEE DETAILS
                    // =================================================

                    + "<h3 style='color:#1f4e79;'>"
                    + "Employee Details"
                    + "</h3>"

                    + "<table style='"
                    + "border-collapse:collapse;"
                    + "width:100%;"
                    + "'>"

                    + row(
                            "Employee ID",
                            employee.getEmployeeCode()
                    )

                    + row(
                            "Name",
                            employee.getName()
                    )

                    + row(
                            "Department",
                            employee.getDepartment()
                    )

                    + row(
                            "Designation",
                            employee.getDesignation()
                    )

                    + row(
                            "Email",
                            employee.getEmail()
                    )

                    + row(
                            "Joining Date",
                            joiningDate
                    )

                    + "</table>"

                    // =================================================
                    // LOGIN DETAILS
                    // =================================================

                    + "<h3 style='color:#1f4e79;'>"
                    + "Login Credentials"
                    + "</h3>"

                    + "<table style='"
                    + "border-collapse:collapse;"
                    + "width:100%;"
                    + "'>"

                    + row(
                            "Login Email",
                            employee.getEmail()
                    )

                    + row(
                            "Temporary Password",
                            temporaryPassword
                    )

                    + "</table>"

                    // =================================================
                    // IMPORTANT MESSAGE
                    // =================================================

                    + "<div style='"
                    + "background:#fff3cd;"
                    + "border:1px solid #ffeeba;"
                    + "padding:12px;"
                    + "margin-top:20px;"
                    + "'>"

                    + "<b>Important:</b> "
                    + "Please change your temporary password "
                    + "after your first login."

                    + "</div>"

                    // =================================================
                    // ID CARD MESSAGE
                    // =================================================

                    + "<p>"
                    + "Your Employee ID Card containing the QR code "
                    + "is attached to this email as a PDF."
                    + "</p>"

                    + "<p>"
                    + "Please keep the ID card safely for official "
                    + "identification purposes."
                    + "</p>"

                    + "<br>"

                    // =================================================
                    // FOOTER
                    // =================================================

                    + "<p>"
                    + "Regards,<br>"
                    + "<b>HR Department</b><br>"
                    + "Future Transformer Company"
                    + "</p>"

                    + "</div>"

                    + "</body>"
                    + "</html>";

            // =================================================
            // 7. SET HTML CONTENT
            // =================================================

            helper.setText(
                    html,
                    true
            );

            // =================================================
            // 8. ATTACH ID CARD PDF
            // =================================================

            helper.addAttachment(
                    employee.getEmployeeCode()
                            + "-ID-Card.pdf",
                    new ByteArrayResource(pdf)
            );

            // =================================================
            // 9. SEND EMAIL
            // =================================================

            System.out.println(
                    "SENDING EMAIL TO: "
                            + employee.getEmail()
            );

            mailSender.send(message);

            System.out.println(
                    "Employee welcome email sent successfully to: "
                            + employee.getEmail()
            );

            System.out.println(
                    "========================================"
            );

        } catch (MessagingException e) {

            System.err.println(
                    "EMAIL SENDING FAILED FOR: "
                            + employee.getEmail()
            );

            e.printStackTrace();

        } catch (Exception e) {

            System.err.println(
                    "ASYNC EMAIL PROCESS FAILED FOR: "
                            + employee.getEmail()
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
                + label
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