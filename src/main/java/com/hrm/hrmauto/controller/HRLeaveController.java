package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.LeaveApplicationResponse;
import com.hrm.hrmauto.service.LeaveApplicationService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/leave")
public class HRLeaveController {

    private final LeaveApplicationService leaveService;

    public HRLeaveController(
            LeaveApplicationService leaveService) {

        this.leaveService = leaveService;
    }

    // ==========================================
    // GET PENDING LEAVES
    // ==========================================

    @GetMapping("/pending")
    public ResponseEntity<
            List<LeaveApplicationResponse>>
    getPendingLeaves() {

        return ResponseEntity.ok(
                leaveService
                        .getPendingApplications()
        );
    }

    // ==========================================
    // APPROVE
    // ==========================================

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveApplicationResponse>
    approveLeave(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveService
                        .approveLeave(id)
        );
    }

    // ==========================================
    // REJECT
    // ==========================================

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveApplicationResponse>
    rejectLeave(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveService
                        .rejectLeave(id)
        );
    }
}