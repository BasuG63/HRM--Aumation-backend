package com.hrm.hrmauto.service;

import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.Payroll;
import com.hrm.hrmauto.repository.PayrollRepository;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PayslipPdfService {

    private final PayrollRepository payrollRepository;

    public PayslipPdfService(
            PayrollRepository payrollRepository) {

        this.payrollRepository =
                payrollRepository;
    }

    // =====================================================
    // GENERATE PAYSLIP PDF
    // =====================================================

    public byte[] generatePayslipPdf(
            Long payrollId) {

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

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            Document document =
                    new Document(
                            PageSize.A4,
                            40,
                            40,
                            40,
                            40
                    );

            PdfWriter.getInstance(
                    document,
                    outputStream
            );

            document.open();

            // =================================================
            // FONTS
            // =================================================

            Font companyFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            20
                    );

            Font titleFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            15
                    );

            Font headingFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            11
                    );

            Font normalFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA,
                            10
                    );

            Font boldFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            10
                    );

            // =================================================
            // COMPANY HEADER
            // =================================================

            Paragraph company =
                    new Paragraph(
                            "HRM COMPANY",
                            companyFont
                    );

            company.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(company);

            Paragraph payslipTitle =
                    new Paragraph(
                            "SALARY PAYSLIP",
                            titleFont
                    );

            payslipTitle.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(payslipTitle);

            String monthName =
                    getMonthName(
                            payroll.getPayMonth()
                    );

            Paragraph payPeriod =
                    new Paragraph(
                            monthName
                                    + " "
                                    + payroll.getPayYear(),
                            normalFont
                    );

            payPeriod.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(payPeriod);

            document.add(
                    new Paragraph(" ")
            );

            // =================================================
            // EMPLOYEE INFORMATION
            // =================================================

            PdfPTable employeeTable =
                    new PdfPTable(2);

            employeeTable.setWidthPercentage(100);

            employeeTable.setWidths(
                    new float[]{
                            35,
                            65
                    }
            );

            addInfoRow(
                    employeeTable,
                    "Employee ID",
                    employee.getEmployeeCode(),
                    boldFont,
                    normalFont
            );

            addInfoRow(
                    employeeTable,
                    "Employee Name",
                    employee.getName(),
                    boldFont,
                    normalFont
            );

            addInfoRow(
                    employeeTable,
                    "Department",
                    employee.getDepartment(),
                    boldFont,
                    normalFont
            );

            addInfoRow(
                    employeeTable,
                    "Designation",
                    employee.getDesignation(),
                    boldFont,
                    normalFont
            );

            addInfoRow(
                    employeeTable,
                    "Pay Period",
                    monthName
                            + " "
                            + payroll.getPayYear(),
                    boldFont,
                    normalFont
            );

            document.add(employeeTable);

            document.add(
                    new Paragraph(" ")
            );

            // =================================================
            // EARNINGS
            // =================================================

            Paragraph earningsHeading =
                    new Paragraph(
                            "EARNINGS",
                            headingFont
                    );

            document.add(earningsHeading);

            PdfPTable earningsTable =
                    new PdfPTable(2);

            earningsTable.setWidthPercentage(100);

            addAmountRow(
                    earningsTable,
                    "Basic Salary",
                    payroll.getBasicSalary(),
                    normalFont
            );

            addAmountRow(
                    earningsTable,
                    "HRA",
                    payroll.getHra(),
                    normalFont
            );

            addAmountRow(
                    earningsTable,
                    "Special Allowance",
                    payroll.getSpecialAllowance(),
                    normalFont
            );

            addAmountRow(
                    earningsTable,
                    "Gross Salary",
                    payroll.getGrossSalary(),
                    boldFont
            );

            document.add(earningsTable);

            document.add(
                    new Paragraph(" ")
            );

            // =================================================
            // DEDUCTIONS
            // =================================================

            Paragraph deductionHeading =
                    new Paragraph(
                            "DEDUCTIONS",
                            headingFont
                    );

            document.add(deductionHeading);

            PdfPTable deductionTable =
                    new PdfPTable(2);

            deductionTable.setWidthPercentage(100);

            addAmountRow(
                    deductionTable,
                    "Provident Fund (PF)",
                    payroll.getPfAmount(),
                    normalFont
            );

            addAmountRow(
                    deductionTable,
                    "ESI",
                    payroll.getEsiAmount(),
                    normalFont
            );

            addAmountRow(
                    deductionTable,
                    "Professional Tax",
                    payroll.getProfessionalTax(),
                    normalFont
            );

            addAmountRow(
                    deductionTable,
                    "Total Deductions",
                    payroll.getTotalDeductions(),
                    boldFont
            );

            document.add(deductionTable);

            document.add(
                    new Paragraph(" ")
            );

            // =================================================
            // NET SALARY
            // =================================================

            PdfPTable netTable =
                    new PdfPTable(2);

            netTable.setWidthPercentage(100);

            PdfPCell netLabel =
                    new PdfPCell(
                            new Phrase(
                                    "NET SALARY",
                                    headingFont
                            )
                    );

            PdfPCell netAmount =
                    new PdfPCell(
                            new Phrase(
                                    formatAmount(
                                            payroll.getNetSalary()
                                    ),
                                    headingFont
                            )
                    );

            netLabel.setPadding(8);
            netAmount.setPadding(8);

            netAmount.setHorizontalAlignment(
                    Element.ALIGN_RIGHT
            );

            netTable.addCell(netLabel);
            netTable.addCell(netAmount);

            document.add(netTable);

            document.add(
                    new Paragraph(" ")
            );

            // =================================================
            // LEAVE SUMMARY
            // =================================================

            Paragraph leaveHeading =
                    new Paragraph(
                            "LEAVE BALANCE SUMMARY",
                            headingFont
                    );

            document.add(leaveHeading);

            PdfPTable leaveTable =
                    new PdfPTable(3);

            leaveTable.setWidthPercentage(100);

            addHeaderCell(
                    leaveTable,
                    "Leave Type",
                    headingFont
            );

            addHeaderCell(
                    leaveTable,
                    "Used",
                    headingFont
            );

            addHeaderCell(
                    leaveTable,
                    "Balance",
                    headingFont
            );

            // CL

            addLeaveRow(
                    leaveTable,
                    "Casual Leave (CL)",
                    payroll.getCasualLeaveUsed(),
                    payroll.getCasualLeave(),
                    normalFont
            );

            // SL

            addLeaveRow(
                    leaveTable,
                    "Sick Leave (SL)",
                    payroll.getSickLeaveUsed(),
                    payroll.getSickLeave(),
                    normalFont
            );

            // EL

            addLeaveRow(
                    leaveTable,
                    "Earned Leave (EL)",
                    payroll.getEarnedLeaveUsed(),
                    payroll.getEarnedLeave(),
                    normalFont
            );

            document.add(leaveTable);

            document.add(
                    new Paragraph(" ")
            );

            // =================================================
            // FOOTER
            // =================================================

            Paragraph footer =
                    new Paragraph(
                            "This is a system generated payslip.",
                            normalFont
                    );

            footer.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(footer);

            Paragraph generated =
                    new Paragraph(
                            "HRM Company - Payroll Department",
                            normalFont
                    );

            generated.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(generated);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate payslip PDF",
                    e
            );
        }
    }

    // =====================================================
    // EMPLOYEE INFORMATION ROW
    // =====================================================

    private void addInfoRow(
            PdfPTable table,
            String label,
            String value,
            Font labelFont,
            Font valueFont) {

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(
                                label,
                                labelFont
                        )
                );

        PdfPCell valueCell =
                new PdfPCell(
                        new Phrase(
                                value != null
                                        ? value
                                        : "",
                                valueFont
                        )
                );

        labelCell.setPadding(6);
        valueCell.setPadding(6);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // =====================================================
    // AMOUNT ROW
    // =====================================================

    private void addAmountRow(
            PdfPTable table,
            String label,
            BigDecimal amount,
            Font font) {

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(
                                label,
                                font
                        )
                );

        PdfPCell amountCell =
                new PdfPCell(
                        new Phrase(
                                formatAmount(amount),
                                font
                        )
                );

        labelCell.setPadding(6);
        amountCell.setPadding(6);

        amountCell.setHorizontalAlignment(
                Element.ALIGN_RIGHT
        );

        table.addCell(labelCell);
        table.addCell(amountCell);
    }

    // =====================================================
    // LEAVE HEADER
    // =====================================================

    private void addHeaderCell(
            PdfPTable table,
            String text,
            Font font) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font
                        )
                );

        cell.setPadding(6);

        table.addCell(cell);
    }

    // =====================================================
    // LEAVE ROW
    // =====================================================

    private void addLeaveRow(
            PdfPTable table,
            String leaveType,
            Integer used,
            Integer balance,
            Font font) {

        table.addCell(
                new PdfPCell(
                        new Phrase(
                                leaveType,
                                font
                        )
                )
        );

        table.addCell(
                new PdfPCell(
                        new Phrase(
                                String.valueOf(
                                        used != null
                                                ? used
                                                : 0
                                ),
                                font
                        )
                )
        );

        table.addCell(
                new PdfPCell(
                        new Phrase(
                                String.valueOf(
                                        balance != null
                                                ? balance
                                                : 0
                                ),
                                font
                        )
                )
        );
    }

    // =====================================================
    // FORMAT AMOUNT
    // =====================================================

    private String formatAmount(
            BigDecimal amount) {

        if (amount == null) {
            return "₹0.00";
        }

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        Locale.US
                );

        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        return "₹"
                + formatter.format(
                        amount
                );
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

        if (month < 1 || month > 12) {
            return "";
        }

        return months[month];
    }
}