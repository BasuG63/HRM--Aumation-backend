package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.EmailDeliveryLogResponse;
import com.hrm.hrmauto.entity.EmailDeliveryLog;
import com.hrm.hrmauto.repository.EmailDeliveryLogRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/email-logs")
public class EmailDeliveryLogController {

    private final EmailDeliveryLogRepository repository;

    public EmailDeliveryLogController(
            EmailDeliveryLogRepository repository) {

        this.repository = repository;
    }

    // =====================================================
    // ALL EMAIL LOGS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<EmailDeliveryLogResponse>>
    getAllLogs() {

        List<EmailDeliveryLogResponse> response =
                repository
                        .findAllByOrderBySentAtDesc()
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // EMAIL LOGS FOR PAYROLL
    // =====================================================

    @GetMapping("/payroll/{payrollId}")
    public ResponseEntity<List<EmailDeliveryLogResponse>>
    getPayrollLogs(
            @PathVariable Long payrollId) {

        List<EmailDeliveryLogResponse> response =
                repository
                        .findByPayrollIdOrderBySentAtDesc(
                                payrollId
                        )
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // SENT COUNT
    // =====================================================

    @GetMapping("/sent/count")
    public ResponseEntity<Long> getSentCount() {

        return ResponseEntity.ok(
                repository.countByStatus("SENT")
        );
    }

    // =====================================================
    // FAILED COUNT
    // =====================================================

    @GetMapping("/failed/count")
    public ResponseEntity<Long> getFailedCount() {

        return ResponseEntity.ok(
                repository.countByStatus("FAILED")
        );
    }

    // =====================================================
    // ENTITY -> DTO
    // =====================================================

    private EmailDeliveryLogResponse convertToResponse(
            EmailDeliveryLog log) {

        Long payrollId = null;
        Long employeeId = null;
        String employeeCode = null;
        String employeeName = null;

        if (log.getPayroll() != null) {

            payrollId =
                    log.getPayroll().getId();
        }

        if (log.getEmployee() != null) {

            employeeId =
                    log.getEmployee().getId();

            employeeCode =
                    log.getEmployee().getEmployeeCode();

            employeeName =
                    log.getEmployee().getName();
        }

        return new EmailDeliveryLogResponse(
                log.getId(),
                payrollId,
                employeeId,
                employeeCode,
                employeeName,
                log.getEmployeeEmail(),
                log.getStatus(),
                log.getErrorMessage(),
                log.getSentAt()
        );
    }
}