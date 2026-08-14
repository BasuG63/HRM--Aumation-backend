package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.PayrollDashboardResponse;
import com.hrm.hrmauto.service.PayrollDashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr/payroll/dashboard")
public class PayrollDashboardController {

    private final PayrollDashboardService dashboardService;


    public PayrollDashboardController(
            PayrollDashboardService dashboardService) {

        this.dashboardService =
                dashboardService;
    }


    // =====================================================
    // HR DASHBOARD
    // =====================================================

    @GetMapping
    public ResponseEntity<PayrollDashboardResponse>
    getDashboard() {

        return ResponseEntity.ok(
                dashboardService.getDashboard()
        );
    }
}