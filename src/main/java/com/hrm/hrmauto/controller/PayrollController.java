package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.PayrollProcessRequest;
import com.hrm.hrmauto.dto.PayrollResponse;
import com.hrm.hrmauto.service.PayrollService;
import com.hrm.hrmauto.service.PayslipEmailService;
import com.hrm.hrmauto.service.PayslipPdfService;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/payroll")
public class PayrollController {

    private final PayrollService payrollService;
    private final PayslipPdfService payslipPdfService;
    private final PayslipEmailService payslipEmailService;


    public PayrollController(
            PayrollService payrollService,
            PayslipPdfService payslipPdfService,
            PayslipEmailService payslipEmailService) {

        this.payrollService = payrollService;
        this.payslipPdfService = payslipPdfService;
        this.payslipEmailService = payslipEmailService;
    }


    // =====================================================
    // PROCESS PAYROLL
    // =====================================================

    @PostMapping("/process")
    public ResponseEntity<PayrollResponse> processPayroll(
            @RequestBody PayrollProcessRequest request) {

        return ResponseEntity.ok(
                payrollService.processPayroll(request)
        );
    }


    // =====================================================
    // GET ALL PAYROLL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<PayrollResponse>> getAllPayroll() {

        /*
         * Get current year/month automatically.
         *
         * This prevents the frontend from being empty
         * when opening Payroll page.
         */

        java.time.LocalDate now =
                java.time.LocalDate.now();

        List<PayrollResponse> payroll =
                payrollService.getMonthlyPayroll(
                        now.getYear(),
                        now.getMonthValue()
                );

        return ResponseEntity.ok(payroll);
    }


    // =====================================================
    // GET PAYROLL BY MONTH
    // =====================================================

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<PayrollResponse>>
    getMonthlyPayroll(
            @PathVariable Integer year,
            @PathVariable Integer month) {

        return ResponseEntity.ok(
                payrollService.getMonthlyPayroll(
                        year,
                        month
                )
        );
    }


    // =====================================================
    // GET PAYROLL BY ID
    // =====================================================

    @GetMapping("/{payrollId}")
    public ResponseEntity<PayrollResponse>
    getPayroll(
            @PathVariable Long payrollId) {

        return ResponseEntity.ok(
                payrollService.getPayroll(
                        payrollId
                )
        );
    }


    // =====================================================
    // DOWNLOAD PAYSLIP PDF
    // =====================================================

    @GetMapping("/{payrollId}/payslip/pdf")
    public ResponseEntity<byte[]> downloadPayslip(
            @PathVariable Long payrollId) {

        byte[] pdf =
                payslipPdfService.generatePayslipPdf(
                        payrollId
                );

        String filename =
                "Payslip-" +
                payrollId +
                ".pdf";

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_PDF
        );

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(filename)
                        .build()
        );

        headers.setContentLength(
                pdf.length
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }


    // =====================================================
    // SEND PAYSLIP EMAIL
    // =====================================================

    @PostMapping("/{payrollId}/payslip/email")
    public ResponseEntity<String>
    sendPayslipEmail(
            @PathVariable Long payrollId) {

        payslipEmailService.sendPayslipEmail(
                payrollId
        );

        return ResponseEntity.ok(
                "Payslip generated and sent successfully to employee email"
        );
    }
}