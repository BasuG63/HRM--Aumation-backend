package com.hrm.hrmauto.controller;

import com.hrm.hrmauto.dto.LeaveApplicationRequest;
import com.hrm.hrmauto.dto.LeaveApplicationResponse;
import com.hrm.hrmauto.dto.LeaveBalanceResponse;
import com.hrm.hrmauto.service.LeaveApplicationService;
import com.hrm.hrmauto.service.LeaveBalanceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/leave")
public class EmployeeLeaveController {

    private final LeaveApplicationService leaveService;

    private final LeaveBalanceService leaveBalanceService;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmployeeLeaveController(
            LeaveApplicationService leaveService,
            LeaveBalanceService leaveBalanceService) {

        this.leaveService =
                leaveService;

        this.leaveBalanceService =
                leaveBalanceService;
    }


    // =====================================================
    // APPLY LEAVE
    // =====================================================

    @PostMapping("/apply")
    public ResponseEntity<LeaveApplicationResponse>
    applyLeave(
            Authentication authentication,
            @RequestBody LeaveApplicationRequest request) {

        String email =
                authentication.getName();


        LeaveApplicationResponse response =
                leaveService.applyLeave(
                        email,
                        request
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =====================================================
    // MY LEAVES
    // =====================================================

    @GetMapping("/my")
    public ResponseEntity<
            List<LeaveApplicationResponse>>
    getMyLeaves(
            Authentication authentication) {

        String email =
                authentication.getName();


        return ResponseEntity.ok(
                leaveService.getMyApplications(
                        email
                )
        );
    }


    // =====================================================
    // MY LEAVE BALANCE
    // =====================================================

    @GetMapping("/balance")
    public ResponseEntity<LeaveBalanceResponse>
    getMyLeaveBalance(
            Authentication authentication) {

        String email =
                authentication.getName();


        LeaveBalanceResponse response =
                leaveBalanceService
                        .getEmployeeLeaveBalanceByEmail(
                                email
                        );


        return ResponseEntity.ok(
                response
        );
    }
}