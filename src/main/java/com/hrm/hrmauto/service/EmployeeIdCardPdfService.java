package com.hrm.hrmauto.service;

import com.hrm.hrmauto.entity.Employee;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class EmployeeIdCardPdfService {

    private final QRCodeService qrCodeService;

    public EmployeeIdCardPdfService(
            QRCodeService qrCodeService) {

        this.qrCodeService = qrCodeService;
    }

    /**
     * Generates an employee ID card PDF
     * containing employee details and QR code.
     */
    public byte[] generateIdCard(Employee employee)
            throws Exception {

        // ========================================
        // Create PDF document
        // ========================================

        try (PDDocument document = new PDDocument()) {

            // ========================================
            // ID Card Size
            // Approx. 85.6mm x 54mm
            // ========================================

            PDRectangle cardSize =
                    new PDRectangle(
                            242.65f,
                            153.07f
                    );

            // ========================================
            // Create Page
            // ========================================

            PDPage page =
                    new PDPage(cardSize);

            document.addPage(page);

            // ========================================
            // Generate QR Code
            // QR contains Employee ID
            // Example: FTC14
            // ========================================

            byte[] qrBytes =
                    qrCodeService.generateQRCode(
                            employee.getEmployeeCode()
                    );

            PDImageXObject qrImage =
                    PDImageXObject.createFromByteArray(
                            document,
                            qrBytes,
                            "employee-qr"
                    );

            // ========================================
            // Draw Content
            // ========================================

            try (PDPageContentStream content =
                         new PDPageContentStream(
                                 document,
                                 page
                         )) {

                float width =
                        cardSize.getWidth();

                float height =
                        cardSize.getHeight();

                // ========================================
                // BACKGROUND
                // ========================================

                setRGB(
                        content,
                        245,
                        248,
                        252
                );

                content.addRect(
                        0,
                        0,
                        width,
                        height
                );

                content.fill();

                // ========================================
                // HEADER
                // ========================================

                setRGB(
                        content,
                        31,
                        78,
                        121
                );

                content.addRect(
                        0,
                        105,
                        width,
                        48
                );

                content.fill();

                // ========================================
                // COMPANY NAME
                // ========================================

                content.beginText();

                content.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA_BOLD
                        ),
                        16
                );

                setRGB(
                        content,
                        255,
                        255,
                        255
                );

                content.newLineAtOffset(
                        18,
                        132
                );

                content.showText(
                        "HRM COMPANY"
                );

                content.endText();

                // ========================================
                // EMPLOYEE ID
                // ========================================

                content.beginText();

                content.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA_BOLD
                        ),
                        10
                );

                setRGB(
                        content,
                        255,
                        255,
                        255
                );

                content.newLineAtOffset(
                        18,
                        116
                );

                content.showText(
                        "EMPLOYEE ID: "
                                + safe(
                                employee.getEmployeeCode()
                        )
                );

                content.endText();

                // ========================================
                // EMPLOYEE NAME
                // ========================================

                content.beginText();

                content.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA_BOLD
                        ),
                        14
                );

                setRGB(
                        content,
                        31,
                        78,
                        121
                );

                content.newLineAtOffset(
                        18,
                        88
                );

                content.showText(
                        safe(
                                employee.getName()
                        )
                );

                content.endText();

                // ========================================
                // EMPLOYEE DETAILS
                // ========================================

                writeText(
                        content,
                        "Designation: "
                                + safe(
                                employee.getDesignation()
                        ),
                        18,
                        70,
                        9
                );

                writeText(
                        content,
                        "Department: "
                                + safe(
                                employee.getDepartment()
                        ),
                        18,
                        56,
                        9
                );

                writeText(
                        content,
                        "Email: "
                                + safe(
                                employee.getEmail()
                        ),
                        18,
                        42,
                        8
                );

                String joiningDate =
                        employee.getJoiningDate() != null
                                ? employee
                                .getJoiningDate()
                                .toString()
                                : "N/A";

                writeText(
                        content,
                        "Joining Date: "
                                + joiningDate,
                        18,
                        28,
                        8
                );

                // ========================================
                // QR CODE
                //
                // IMPORTANT:
                // QR must NOT be inside beginText()
                // ========================================

                content.drawImage(
                        qrImage,
                        170,
                        30,
                        50,
                        50
                );

                // ========================================
                // FOOTER
                // ========================================

                setRGB(
                        content,
                        31,
                        78,
                        121
                );

                content.addRect(
                        0,
                        0,
                        width,
                        15
                );

                content.fill();

                content.beginText();

                content.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA
                        ),
                        7
                );

                setRGB(
                        content,
                        255,
                        255,
                        255
                );

                content.newLineAtOffset(
                        18,
                        5
                );

                content.showText(
                        "Authorized Employee Identification Card"
                );

                content.endText();
            }

            // ========================================
            // Convert PDF to byte[]
            // ========================================

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            document.save(output);

            return output.toByteArray();
        }
    }

    // ========================================
    // WRITE TEXT
    // ========================================

    private void writeText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float fontSize)
            throws IOException {

        content.beginText();

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
                ),
                fontSize
        );

        setRGB(
                content,
                50,
                50,
                50
        );

        content.newLineAtOffset(
                x,
                y
        );

        content.showText(text);

        content.endText();
    }

    // ========================================
    // RGB COLOR HELPER
    // PDFBox expects values between 0 and 1
    // ========================================

    private void setRGB(
            PDPageContentStream content,
            int r,
            int g,
            int b)
            throws IOException {

        content.setNonStrokingColor(
                r / 255f,
                g / 255f,
                b / 255f
        );
    }

    // ========================================
    // NULL SAFE
    // ========================================

    private String safe(String value) {

        return value == null
                ? "N/A"
                : value;
    }
}