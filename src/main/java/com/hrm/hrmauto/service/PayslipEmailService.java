package com.hrm.hrmauto.service;

import com.hrm.hrmauto.entity.EmailDeliveryLog;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.Payroll;
import com.hrm.hrmauto.repository.EmailDeliveryLogRepository;
import com.hrm.hrmauto.repository.PayrollRepository;

import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class PayslipEmailService {

    private final EmailDeliveryLogRepository emailDeliveryLogRepository;
    private final JavaMailSender mailSender;
    private final PayrollRepository payrollRepository;
    private final PayslipPdfService payslipPdfService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PayslipEmailService(
            JavaMailSender mailSender,
            PayrollRepository payrollRepository,
            PayslipPdfService payslipPdfService,
            EmailDeliveryLogRepository emailDeliveryLogRepository) {

        this.mailSender = mailSender;
        this.payrollRepository = payrollRepository;
        this.payslipPdfService = payslipPdfService;
        this.emailDeliveryLogRepository =
                emailDeliveryLogRepository;
    }

    // =====================================================
    // SEND PAYSLIP EMAIL
    // =====================================================

    public void sendPayslipEmail(Long payrollId) {

        Payroll payroll =
                payrollRepository
                        .findById(payrollId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payroll not found with ID "
                                                + payrollId
                                )
                        );

        Employee employee =
                payroll.getEmployee();

        if (employee == null) {

            throw new RuntimeException(
                    "Employee not found for payroll "
                            + payrollId
            );
        }

        // =================================================
        // CHECK EMAIL
        // =================================================

        if (employee.getEmail() == null ||
                employee.getEmail().trim().isEmpty()) {

            payroll.setEmailStatus("FAILED");

            payrollRepository.save(payroll);

            saveEmailLog(
                    payroll,
                    employee,
                    "FAILED",
                    "Employee email address is not available"
            );

            throw new RuntimeException(
                    "Employee email address is not available"
            );
        }

        try {

            // =================================================
            // GENERATE PDF
            // =================================================

            byte[] pdf =
                    payslipPdfService
                            .generatePayslipPdf(
                                    payrollId
                            );

            // =================================================
            // CREATE EMAIL
            // =================================================

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            // =================================================
            // MONTH
            // =================================================

            String monthName =
                    getMonthName(
                            payroll.getPayMonth()
                    );

            // =================================================
            // RECIPIENT
            // =================================================

            helper.setTo(
                    employee.getEmail()
            );

            // =================================================
            // SUBJECT
            // =================================================

            helper.setSubject(
                    "Salary Payslip - "
                            + monthName
                            + " "
                            + payroll.getPayYear()
            );

            // =================================================
            // PROFESSIONAL HTML EMAIL
            //
            // IMPORTANT:
            // DO NOT USE .formatted()
            // because CSS contains % characters.
            // =================================================

            String htmlBody = """

                    <!DOCTYPE html>

                    <html>

                    <head>

                        <meta charset="UTF-8">

                        <meta name="viewport"
                              content="width=device-width,
                              initial-scale=1.0">

                        <title>Salary Payslip</title>

                    </head>


                    <body style="
                        margin:0;
                        padding:0;
                        background:#f3f6f9;
                        font-family:Arial,
                        Helvetica,
                        sans-serif;
                    ">


                    <!-- OUTER CONTAINER -->

                    <div style="
                        width:100%;
                        background:#f3f6f9;
                        padding:30px 0;
                    ">


                        <!-- EMAIL CARD -->

                        <div style="
                            max-width:720px;
                            margin:0 auto;
                            background:#ffffff;
                            border:1px solid #dfe4ea;
                            border-radius:8px;
                            overflow:hidden;
                            box-shadow:
                                0 3px 12px
                                rgba(0,0,0,0.08);
                        ">


                            <!-- =================================================
                                 COMPANY LETTERHEAD
                            ================================================= -->

                            <div style="
                                padding:25px 30px;
                                background:#ffffff;
                                border-bottom:4px solid #1f4e79;
                            ">

                                <table width="100%"
                                       cellpadding="0"
                                       cellspacing="0"
                                       border="0">

                                    <tr>

                                        <!-- LOGO -->

                                        <td width="110"
                                            valign="middle"
                                            align="left">

                                            <img
                                                src="cid:companyLogo"
                                                alt="Company Logo"
                                                style="
                                                    width:90px;
                                                    max-width:90px;
                                                    height:auto;
                                                    display:block;
                                                "
                                            >

                                        </td>


                                        <!-- COMPANY INFORMATION -->

                                        <td valign="middle"
                                            style="
                                                padding-left:15px;
                                            ">

                                            <div style="
                                                font-size:24px;
                                                font-weight:bold;
                                                color:#1f2937;
                                                margin-bottom:5px;
                                            ">

                                                Feuture Transformation

                                            </div>


                                            <div style="
                                                font-size:14px;
                                                color:#1f4e79;
                                                font-weight:bold;
                                                margin-bottom:5px;
                                            ">

                                                Human Resource
                                                Management Department

                                            </div>


                                            <div style="
                                                font-size:12px;
                                                color:#6b7280;
                                                line-height:18px;
                                            ">

                                                Company Address,
                                                Karnataka, India

                                                <br>

                                                Email:
                                                hr@company.com

                                                &nbsp; | &nbsp;

                                                Phone:
                                                +91 XXXXX XXXXX

                                            </div>

                                        </td>

                                    </tr>

                                </table>

                            </div>


                            <!-- =================================================
                                 PAYSLIP TITLE
                            ================================================= -->

                            <div style="
                                padding:30px 35px 10px 35px;
                                text-align:center;
                            ">

                                <div style="
                                    font-size:24px;
                                    font-weight:bold;
                                    color:#1f4e79;
                                    letter-spacing:0.5px;
                                ">

                                    SALARY PAYSLIP

                                </div>


                                <div style="
                                    margin-top:8px;
                                    font-size:14px;
                                    color:#6b7280;
                                ">

                                    {{MONTH}} {{YEAR}}

                                </div>

                            </div>


                            <!-- =================================================
                                 MAIN CONTENT
                            ================================================= -->

                            <div style="
                                padding:20px 35px 35px 35px;
                            ">


                                <!-- GREETING -->

                                <p style="
                                    font-size:15px;
                                    color:#374151;
                                    margin-bottom:8px;
                                ">

                                    Dear
                                    <strong>
                                        {{EMPLOYEE_NAME}}
                                    </strong>,

                                </p>


                                <p style="
                                    font-size:14px;
                                    color:#4b5563;
                                    line-height:22px;
                                ">

                                    Please find attached your salary
                                    payslip for
                                    <strong>
                                        {{MONTH}} {{YEAR}}
                                    </strong>.

                                    This document contains your salary
                                    and deduction details for the
                                    mentioned payroll period.

                                </p>


                                <!-- =================================================
                                     EMPLOYEE INFORMATION
                                ================================================= -->

                                <div style="
                                    margin-top:28px;
                                    margin-bottom:10px;
                                    font-size:17px;
                                    font-weight:bold;
                                    color:#1f4e79;
                                ">

                                    Employee Information

                                </div>


                                <table width="100%"
                                       cellpadding="0"
                                       cellspacing="0"
                                       border="0"
                                       style="
                                            border-collapse:collapse;
                                            border:1px solid #d9dee5;
                                       ">


                                    <!-- EMPLOYEE ID -->

                                    <tr>

                                        <td style="
                                            width:40%;
                                            padding:12px;
                                            background:#f8fafc;
                                            border:1px solid #d9dee5;
                                            font-weight:bold;
                                            color:#374151;
                                        ">

                                            Employee ID

                                        </td>


                                        <td style="
                                            padding:12px;
                                            border:1px solid #d9dee5;
                                            color:#4b5563;
                                        ">

                                            {{EMPLOYEE_CODE}}

                                        </td>

                                    </tr>


                                    <!-- NAME -->

                                    <tr>

                                        <td style="
                                            padding:12px;
                                            background:#f8fafc;
                                            border:1px solid #d9dee5;
                                            font-weight:bold;
                                            color:#374151;
                                        ">

                                            Employee Name

                                        </td>


                                        <td style="
                                            padding:12px;
                                            border:1px solid #d9dee5;
                                            color:#4b5563;
                                        ">

                                            {{EMPLOYEE_NAME}}

                                        </td>

                                    </tr>


                                    <!-- DEPARTMENT -->

                                    <tr>

                                        <td style="
                                            padding:12px;
                                            background:#f8fafc;
                                            border:1px solid #d9dee5;
                                            font-weight:bold;
                                            color:#374151;
                                        ">

                                            Department

                                        </td>


                                        <td style="
                                            padding:12px;
                                            border:1px solid #d9dee5;
                                            color:#4b5563;
                                        ">

                                            {{DEPARTMENT}}

                                        </td>

                                    </tr>


                                    <!-- DESIGNATION -->

                                    <tr>

                                        <td style="
                                            padding:12px;
                                            background:#f8fafc;
                                            border:1px solid #d9dee5;
                                            font-weight:bold;
                                            color:#374151;
                                        ">

                                            Designation

                                        </td>


                                        <td style="
                                            padding:12px;
                                            border:1px solid #d9dee5;
                                            color:#4b5563;
                                        ">

                                            {{DESIGNATION}}

                                        </td>

                                    </tr>


                                </table>


                                <!-- =================================================
                                     SALARY SUMMARY
                                ================================================= -->

                                <div style="
                                    margin-top:30px;
                                    margin-bottom:10px;
                                    font-size:17px;
                                    font-weight:bold;
                                    color:#1f4e79;
                                ">

                                    Salary Summary

                                </div>


                                <table width="100%"
                                       cellpadding="0"
                                       cellspacing="0"
                                       border="0"
                                       style="
                                            border-collapse:collapse;
                                            border:1px solid #d9dee5;
                                       ">


                                    <!-- GROSS SALARY -->

                                    <tr>

                                        <td style="
                                            padding:13px;
                                            border:1px solid #d9dee5;
                                            color:#374151;
                                        ">

                                            Gross Salary

                                        </td>


                                        <td align="right"
                                            style="
                                                padding:13px;
                                                border:1px solid #d9dee5;
                                                color:#374151;
                                            ">

                                            ₹{{GROSS_SALARY}}

                                        </td>

                                    </tr>


                                    <!-- TOTAL DEDUCTIONS -->

                                    <tr>

                                        <td style="
                                            padding:13px;
                                            border:1px solid #d9dee5;
                                            color:#374151;
                                        ">

                                            Total Deductions

                                        </td>


                                        <td align="right"
                                            style="
                                                padding:13px;
                                                border:1px solid #d9dee5;
                                                color:#374151;
                                            ">

                                            ₹{{TOTAL_DEDUCTIONS}}

                                        </td>

                                    </tr>


                                    <!-- NET SALARY -->

                                    <tr>

                                        <td style="
                                            padding:15px;
                                            border:1px solid #d9dee5;
                                            background:#eef5fb;
                                            color:#1f4e79;
                                            font-weight:bold;
                                            font-size:15px;
                                        ">

                                            Net Salary

                                        </td>


                                        <td align="right"
                                            style="
                                                padding:15px;
                                                border:1px solid #d9dee5;
                                                background:#eef5fb;
                                                color:#1f4e79;
                                                font-weight:bold;
                                                font-size:18px;
                                            ">

                                            ₹{{NET_SALARY}}

                                        </td>

                                    </tr>


                                </table>


                                <!-- =================================================
                                     ATTACHMENT NOTICE
                                ================================================= -->

                                <div style="
                                    margin-top:25px;
                                    padding:15px;
                                    background:#f8fafc;
                                    border-left:4px solid #1f4e79;
                                ">

                                    <div style="
                                        font-size:13px;
                                        color:#4b5563;
                                        line-height:20px;
                                    ">

                                        <strong>
                                            Payslip Attached
                                        </strong>

                                        <br>

                                        Your detailed salary payslip
                                        is attached to this email
                                        as a PDF document.

                                    </div>

                                </div>


                                <!-- =================================================
                                     SUPPORT MESSAGE
                                ================================================= -->

                                <p style="
                                    margin-top:28px;
                                    font-size:14px;
                                    color:#374151;
                                    line-height:21px;
                                ">

                                    If you have any questions regarding
                                    your salary or deductions, please
                                    contact the HR department.

                                </p>


                                <!-- =================================================
                                     REGARDS
                                ================================================= -->

                                <p style="
                                    margin-top:25px;
                                    font-size:14px;
                                    color:#374151;
                                ">

                                    Regards,

                                    <br>

                                    <strong>
                                        HR Department
                                    </strong>

                                    <br>

                                    YOUR COMPANY NAME

                                </p>


                            </div>


                            <!-- =================================================
                                 FOOTER
                            ================================================= -->

                            <div style="
                                padding:20px 30px;
                                background:#f8fafc;
                                border-top:1px solid #e5e7eb;
                                text-align:center;
                            ">

                                <div style="
                                    font-size:11px;
                                    color:#6b7280;
                                    line-height:18px;
                                ">

                                    This is a system-generated email.

                                    <br>

                                    Please do not reply directly
                                    to this email.

                                    <br><br>

                                    © {{YEAR}}
                                    YOUR COMPANY NAME.
                                    All rights reserved.

                                </div>

                            </div>


                        </div>


                    </div>


                    </body>

                    </html>

                    """;


            // =================================================
            // REPLACE HTML PLACEHOLDERS
            //
            // No String.formatted()
            // Therefore CSS % characters are safe.
            // =================================================

            htmlBody =
                    htmlBody
                            .replace(
                                    "{{MONTH}}",
                                    safe(monthName)
                            )

                            .replace(
                                    "{{YEAR}}",
                                    safe(
                                            payroll.getPayYear()
                                    )
                            )

                            .replace(
                                    "{{EMPLOYEE_NAME}}",
                                    safe(
                                            employee.getName()
                                    )
                            )

                            .replace(
                                    "{{EMPLOYEE_CODE}}",
                                    safe(
                                            employee.getEmployeeCode()
                                    )
                            )

                            .replace(
                                    "{{DEPARTMENT}}",
                                    safe(
                                            employee.getDepartment()
                                    )
                            )

                            .replace(
                                    "{{DESIGNATION}}",
                                    safe(
                                            employee.getDesignation()
                                    )
                            )

                            .replace(
                                    "{{GROSS_SALARY}}",
                                    safe(
                                            payroll.getGrossSalary()
                                    )
                            )

                            .replace(
                                    "{{TOTAL_DEDUCTIONS}}",
                                    safe(
                                            payroll.getTotalDeductions()
                                    )
                            )

                            .replace(
                                    "{{NET_SALARY}}",
                                    safe(
                                            payroll.getNetSalary()
                                    )
                            );


            // =================================================
            // SET HTML EMAIL
            // =================================================

            helper.setText(
                    htmlBody,
                    true
            );


            // =================================================
            // ADD COMPANY LOGO
            // =================================================

            ClassPathResource logo =
                    new ClassPathResource(
                            "static/images/company-logo.webp"
                    );


            if (logo.exists()) {

                helper.addInline(
                        "companyLogo",
                        logo
                );

            } else {

                System.err.println(
                        "WARNING: Company logo not found: "
                                + "static/images/company-logo.png"
                );
            }


            // =================================================
            // ATTACH PDF
            // =================================================

            String filename =
                    "Payslip-"
                            + employee.getEmployeeCode()
                            + "-"
                            + payroll.getPayMonth()
                            + "-"
                            + payroll.getPayYear()
                            + ".pdf";


            helper.addAttachment(
                    filename,
                    new ByteArrayResource(pdf)
            );


            // =================================================
            // SEND EMAIL
            // =================================================

            // IMPORTANT:
            // This must execute only ONCE.

            mailSender.send(message);


            // =================================================
            // MARK PAYROLL EMAIL AS SENT
            // =================================================

            payroll.setEmailStatus(
                    "SENT"
            );

            payrollRepository.save(
                    payroll
            );


            // =================================================
            // SAVE EMAIL LOG
            // =================================================

            saveEmailLog(
                    payroll,
                    employee,
                    "SENT",
                    null
            );


            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "PAYSLIP EMAIL SENT SUCCESSFULLY"
            );

            System.out.println(
                    "Employee: "
                            + employee.getName()
            );

            System.out.println(
                    "Email: "
                            + employee.getEmail()
            );

            System.out.println(
                    "Payroll ID: "
                            + payrollId
            );

            System.out.println(
                    "========================================"
            );


        } catch (Exception e) {

            // =================================================
            // MARK FAILED
            // =================================================

            payroll.setEmailStatus(
                    "FAILED"
            );

            payrollRepository.save(
                    payroll
            );


            // =================================================
            // SAVE FAILED LOG
            // =================================================

            saveEmailLog(
                    payroll,
                    employee,
                    "FAILED",
                    e.getMessage()
            );


            System.err.println(
                    "========================================"
            );

            System.err.println(
                    "PAYSLIP EMAIL FAILED"
            );

            System.err.println(
                    "Employee: "
                            + employee.getName()
            );

            System.err.println(
                    "Email: "
                            + employee.getEmail()
            );

            System.err.println(
                    "Payroll ID: "
                            + payrollId
            );

            System.err.println(
                    "Reason: "
                            + e.getMessage()
            );

            System.err.println(
                    "========================================"
            );


            throw new RuntimeException(
                    "Payslip email could not be sent to "
                            + employee.getEmail(),
                    e
            );
        }
    }


    // =====================================================
    // SAVE EMAIL DELIVERY LOG
    // =====================================================

    private void saveEmailLog(
            Payroll payroll,
            Employee employee,
            String status,
            String errorMessage) {

        try {

            EmailDeliveryLog log =
                    new EmailDeliveryLog();


            log.setPayroll(
                    payroll
            );


            log.setEmployee(
                    employee
            );


            log.setEmployeeEmail(
                    employee.getEmail()
            );


            log.setStatus(
                    status
            );


            log.setErrorMessage(
                    errorMessage
            );


            emailDeliveryLogRepository.save(
                    log
            );


            System.out.println(
                    "Email delivery log saved: "
                            + status
            );


        } catch (Exception logException) {

            System.err.println(
                    "Failed to save email delivery log: "
                            + logException.getMessage()
            );
        }
    }


    // =====================================================
    // SAFE VALUE
    // =====================================================

    private String safe(
            Object value) {

        return value == null
                ? ""
                : String.valueOf(value);
    }


    // =====================================================
    // MONTH NAME
    // =====================================================

    private String getMonthName(
            Integer month) {

        if (month == null) {

            return "";
        }


        String[] months = {

                "",

                "January",

                "February",

                "March",

                "April",

                "May",

                "June",

                "July",

                "August",

                "September",

                "October",

                "November",

                "December"
        };


        if (month < 1 ||
                month > 12) {

            return "";
        }


        return months[month];
    }
}