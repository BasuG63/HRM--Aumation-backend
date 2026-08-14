package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.LeaveBalanceResponse;
import com.hrm.hrmauto.service.LeaveBalanceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(
            LeaveBalanceService leaveBalanceService) {

        this.leaveBalanceService =
                leaveBalanceService;
    }

    @GetMapping("/{employeeId}/leave-balance")
    public ResponseEntity<LeaveBalanceResponse>
    getLeaveBalance(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                leaveBalanceService
                        .getEmployeeLeaveBalance(
                                employeeId
                        )
        );
    }
}