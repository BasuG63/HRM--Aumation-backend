package com.hrm.hrmauto.service;
import java.time.Year;
import com.hrm.hrmauto.dto.LeaveApplicationRequest;
import com.hrm.hrmauto.dto.LeaveApplicationResponse;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.LeaveApplication;
import com.hrm.hrmauto.entity.LeaveBalance;
import com.hrm.hrmauto.entity.LeaveStatus;
import com.hrm.hrmauto.entity.LeaveType;
import com.hrm.hrmauto.exception.BadRequestException;
import com.hrm.hrmauto.repository.EmployeeRepository;
import com.hrm.hrmauto.repository.LeaveApplicationRepository;
import com.hrm.hrmauto.repository.LeaveBalanceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveApplicationService {
	private final LeaveEmailService leaveEmailService;

    private final EmployeeRepository employeeRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveApplicationService(
            EmployeeRepository employeeRepository,
            LeaveApplicationRepository leaveApplicationRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            LeaveEmailService leaveEmailService) {

        this.employeeRepository = employeeRepository;

        this.leaveApplicationRepository =
                leaveApplicationRepository;

        this.leaveBalanceRepository =
                leaveBalanceRepository;

        this.leaveEmailService =
                leaveEmailService;
    }

    // =====================================================
    // EMPLOYEE APPLY LEAVE
    // =====================================================

 // =====================================================
 // EMPLOYEE APPLY LEAVE
 // =====================================================

 // =====================================================
 // EMPLOYEE APPLY LEAVE
 // =====================================================

 @Transactional
 public LeaveApplicationResponse applyLeave(
         String email,
         LeaveApplicationRequest request) {

     // =====================================================
     // 1. VALIDATE LEAVE TYPE
     // =====================================================

     if (request.getLeaveType() == null) {

         throw new BadRequestException(
                 "Leave type is required"
         );
     }

     // =====================================================
     // 2. VALIDATE DATES
     // =====================================================

     if (request.getStartDate() == null ||
             request.getEndDate() == null) {

         throw new BadRequestException(
                 "Start date and end date are required"
         );
     }

     // =====================================================
     // 3. VALIDATE DATE ORDER
     // =====================================================

     if (request.getStartDate()
             .isAfter(request.getEndDate())) {

         throw new BadRequestException(
                 "Start date cannot be after end date"
         );
     }

     // =====================================================
     // 4. PREVENT PAST DATE LEAVE
     // =====================================================

     if (request.getStartDate()
             .isBefore(java.time.LocalDate.now())) {

         throw new BadRequestException(
                 "Leave cannot be applied for a past date"
         );
     }

     // =====================================================
     // 5. VALIDATE REASON
     // =====================================================

     if (request.getReason() == null ||
             request.getReason().isBlank()) {

         throw new BadRequestException(
                 "Leave reason is required"
         );
     }

     // =====================================================
     // 6. FIND EMPLOYEE USING JWT EMAIL
     // =====================================================

     Employee employee =
    	        employeeRepository
    	                .findByEmail(email)
    	                .orElseThrow(() ->
    	                        new RuntimeException(
    	                                "Employee not found with email: " + email
    	                        )
    	                );
     // =====================================================
     // 7. CHECK OVERLAPPING LEAVE
     //
     // Existing PENDING or APPROVED leave
     // should prevent another leave for
     // overlapping dates.
     //
     // REJECTED leaves are ignored.
     // =====================================================
  // =====================================================
  // CHECK OVERLAPPING LEAVE
  // =====================================================

  boolean overlappingLeave =
          leaveApplicationRepository
                  .existsByEmployeeIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                          employee.getId(),

                          List.of(
                                  LeaveStatus.PENDING,
                                  LeaveStatus.APPROVED
                          ),

                          request.getEndDate(),

                          request.getStartDate()
                  );

  if (overlappingLeave) {

      throw new BadRequestException(
              "You already have a pending or approved leave "
                      + "for the selected dates"
      );
  }

     // =====================================================
     // 8. CALCULATE NUMBER OF DAYS
     //
     // Example:
     // 20-Aug to 22-Aug = 3 days
     // =====================================================

     int numberOfDays =
             (int) ChronoUnit.DAYS.between(
                     request.getStartDate(),
                     request.getEndDate()
             ) + 1;

     // =====================================================
     // 9. GET CURRENT YEAR
     // =====================================================

     int currentYear =
             Year.now().getValue();

     // =====================================================
     // 10. FIND EMPLOYEE LEAVE BALANCE
     // =====================================================

     LeaveBalance balance =
             leaveBalanceRepository
                     .findByEmployeeIdAndYear(
                             employee.getId(),
                             currentYear
                     )
                     .orElseThrow(() ->
                             new BadRequestException(
                                     "Leave balance not found for year "
                                             + currentYear
                             )
                     );

     // =====================================================
     // 11. CHECK AVAILABLE LEAVE BALANCE
     // =====================================================

     int available =
             getAvailableBalance(
                     balance,
                     request.getLeaveType()
             );

     // =====================================================
     // 12. CHECK SUFFICIENT BALANCE
     // =====================================================

     if (numberOfDays > available) {

         throw new BadRequestException(
                 "Insufficient "
                         + request.getLeaveType()
                         + " leave balance. Available: "
                         + available
         );
     }

     // =====================================================
     // 13. CREATE LEAVE APPLICATION
     // =====================================================

     LeaveApplication application =
             new LeaveApplication();

     application.setEmployee(
             employee
     );

     application.setLeaveType(
             request.getLeaveType()
     );

     application.setStartDate(
             request.getStartDate()
     );

     application.setEndDate(
             request.getEndDate()
     );

     application.setNumberOfDays(
             numberOfDays
     );

     application.setReason(
             request.getReason().trim()
     );

     // =====================================================
     // 14. SET INITIAL STATUS
     //
     // IMPORTANT:
     // Balance is NOT deducted here.
     // Balance is deducted only when HR approves.
     // =====================================================

     application.setStatus(
             LeaveStatus.PENDING
     );

     // =====================================================
     // 15. SAVE LEAVE APPLICATION
     // =====================================================

     LeaveApplication saved =
             leaveApplicationRepository.save(
                     application
             );

     // =====================================================
     // 16. RETURN RESPONSE
     // =====================================================

     return convertToResponse(
             saved
     );
 }
    // =====================================================
    // EMPLOYEE - GET MY LEAVE APPLICATIONS
    // =====================================================

    public List<LeaveApplicationResponse>
    getMyApplications(String email) {

        Employee employee =
                employeeRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee profile not found"
                                )
                        );

        return leaveApplicationRepository
                .findByEmployeeIdOrderByAppliedAtDesc(
                        employee.getId()
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // HR - GET PENDING APPLICATIONS
    // =====================================================

    public List<LeaveApplicationResponse>
    getPendingApplications() {

        return leaveApplicationRepository
                .findByStatusOrderByAppliedAtDesc(
                        LeaveStatus.PENDING
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // HR - APPROVE LEAVE
    // =====================================================

    @Transactional
    public LeaveApplicationResponse approveLeave(
            Long applicationId) {

        // Find application

        LeaveApplication application =
                leaveApplicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave application not found"
                                )
                        );

        // Only PENDING applications can be approved

        if (application.getStatus()
                != LeaveStatus.PENDING) {

            throw new BadRequestException(
                    "Leave application is already "
                            + application.getStatus()
            );
        }

        // Find employee balance

        int currentYear =
                Year.now().getValue();

        LeaveBalance balance =
                leaveBalanceRepository
                        .findByEmployeeIdAndYear(
                                application
                                        .getEmployee()
                                        .getId(),
                                currentYear
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave balance not found for year "
                                                + currentYear
                                )
                        
                        );

        // Get available balance

        int available =
                getAvailableBalance(
                        balance,
                        application.getLeaveType()
                );

        int days =
                application.getNumberOfDays();

        // Re-check balance before deduction

        if (days > available) {

            throw new RuntimeException(
                    "Insufficient "
                            + application.getLeaveType()
                            + " balance. Available: "
                            + available
            );
        }

        // Deduct leave

        deductLeave(
                balance,
                application.getLeaveType(),
                days
        );

        leaveBalanceRepository.save(balance);

        // Update application status

        application.setStatus(
                LeaveStatus.APPROVED
        );

        application.setReviewedAt(
                LocalDateTime.now()
        );

        LeaveApplication saved =
                leaveApplicationRepository.save(
                        application
                );

        // ==========================================
        // SEND APPROVAL EMAIL
        // ==========================================

        try {

            leaveEmailService.sendLeaveApprovedEmail(
                    saved
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send leave approval email: "
                            + e.getMessage()
            );
        }

        return convertToResponse(saved);
    }

    // =====================================================
    // HR - REJECT LEAVE
    // =====================================================

    @Transactional
    public LeaveApplicationResponse rejectLeave(
            Long applicationId) {

        LeaveApplication application =
                leaveApplicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Leave application not found"
                                )
                        );

        // Only PENDING applications can be rejected

        if (application.getStatus()
                != LeaveStatus.PENDING) {

            throw new RuntimeException(
                    "Leave application is already "
                            + application.getStatus()
            );
        }

        // IMPORTANT:
        // Do NOT deduct leave balance when rejected.

        application.setStatus(
                LeaveStatus.REJECTED
        );

        application.setReviewedAt(
                LocalDateTime.now()
        );
        LeaveApplication saved =
                leaveApplicationRepository.save(
                        application
                );

        // ==========================================
        // SEND REJECTION EMAIL
        // ==========================================

        try {

            leaveEmailService.sendLeaveRejectedEmail(
                    saved
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send leave rejection email: "
                            + e.getMessage()
            );
        }

        return convertToResponse(saved);
    }

    // =====================================================
    // GET AVAILABLE BALANCE
    // =====================================================

    private int getAvailableBalance(
            LeaveBalance balance,
            LeaveType leaveType) {

        return switch (leaveType) {

            case CL ->
                    balance.getCasualLeave();

            case SL ->
                    balance.getSickLeave();

            case EL ->
                    balance.getEarnedLeave();
        };
    }

    // =====================================================
    // DEDUCT LEAVE
    // =====================================================

    private void deductLeave(
            LeaveBalance balance,
            LeaveType leaveType,
            int days) {

        switch (leaveType) {

            case CL:

                balance.setCasualLeave(
                        balance.getCasualLeave() - days
                );

                break;

            case SL:

                balance.setSickLeave(
                        balance.getSickLeave() - days
                );

                break;

            case EL:

                balance.setEarnedLeave(
                        balance.getEarnedLeave() - days
                );

                break;
        }
    }

    // =====================================================
    // RESPONSE CONVERTER
    // =====================================================

    private LeaveApplicationResponse
    convertToResponse(
            LeaveApplication application) {

        return new LeaveApplicationResponse(

                application.getId(),

                application
                        .getEmployee()
                        .getEmployeeCode(),

                application
                        .getEmployee()
                        .getName(),

                application.getLeaveType(),

                application.getStartDate(),

                application.getEndDate(),

                application.getNumberOfDays(),

                application.getReason(),

                application.getStatus()
        );
    }
}