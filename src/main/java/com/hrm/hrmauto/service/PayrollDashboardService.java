package com.hrm.hrmauto.service;

import com.hrm.hrmauto.dto.PayrollDashboardResponse;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.LeaveBalance;
import com.hrm.hrmauto.entity.Payroll;
import com.hrm.hrmauto.entity.PayrollStatus;
import com.hrm.hrmauto.repository.EmailDeliveryLogRepository;
import com.hrm.hrmauto.repository.EmployeeRepository;
import com.hrm.hrmauto.repository.LeaveBalanceRepository;
import com.hrm.hrmauto.repository.PayrollRepository;

import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class PayrollDashboardService {

    private final EmployeeRepository employeeRepository;

    private final PayrollRepository payrollRepository;

    private final EmailDeliveryLogRepository
            emailDeliveryLogRepository;

    private final LeaveBalanceRepository
            leaveBalanceRepository;


    public PayrollDashboardService(
            EmployeeRepository employeeRepository,
            PayrollRepository payrollRepository,
            EmailDeliveryLogRepository
                    emailDeliveryLogRepository,
            LeaveBalanceRepository leaveBalanceRepository) {

        this.employeeRepository =
                employeeRepository;

        this.payrollRepository =
                payrollRepository;

        this.emailDeliveryLogRepository =
                emailDeliveryLogRepository;

        this.leaveBalanceRepository =
                leaveBalanceRepository;
    }


    // =====================================================
    // HR PAYROLL DASHBOARD
    // =====================================================

    public PayrollDashboardResponse
    getDashboard() {

        // -------------------------------------------------
        // TOTAL EMPLOYEES PROCESSED
        // -------------------------------------------------

        long totalEmployeesProcessed =
                payrollRepository
                        .countByStatus(
                                PayrollStatus.PROCESSED
                        );


        // -------------------------------------------------
        // PENDING PAYROLL
        // -------------------------------------------------

        long pendingPayroll =
                payrollRepository
                        .countByStatus(
                                PayrollStatus.PENDING
                        );


        // -------------------------------------------------
        // EMAIL SENT
        // -------------------------------------------------

        long payslipsSentSuccessfully =
                emailDeliveryLogRepository
                        .countByStatus("SENT");


        // -------------------------------------------------
        // EMAIL FAILED
        // -------------------------------------------------

        long failedEmailDeliveries =
                emailDeliveryLogRepository
                        .countByStatus("FAILED");


        // -------------------------------------------------
        // CURRENT YEAR LEAVE BALANCES
        // -------------------------------------------------

        int currentYear =
                Year.now().getValue();

        List<LeaveBalance> balances =
                leaveBalanceRepository
                        .findByYear(currentYear);


        long totalCasualLeave = 0;
        long totalSickLeave = 0;
        long totalEarnedLeave = 0;

        long casualLeaveUsed = 0;
        long sickLeaveUsed = 0;
        long earnedLeaveUsed = 0;


        for (LeaveBalance balance : balances) {

            totalCasualLeave +=
                    balance.getCasualLeave();

            totalSickLeave +=
                    balance.getSickLeave();

            totalEarnedLeave +=
                    balance.getEarnedLeave();
        }


        /*
         * Used leave is calculated from:
         *
         * allocated - remaining
         */

        for (LeaveBalance balance : balances) {

            casualLeaveUsed +=
                    12 - balance.getCasualLeave();

            sickLeaveUsed +=
                    12 - balance.getSickLeave();

            earnedLeaveUsed +=
                    15 - balance.getEarnedLeave();
        }


        return new PayrollDashboardResponse(

                totalEmployeesProcessed,

                pendingPayroll,

                payslipsSentSuccessfully,

                failedEmailDeliveries,

                totalCasualLeave,

                totalSickLeave,

                totalEarnedLeave,

                casualLeaveUsed,

                sickLeaveUsed,

                earnedLeaveUsed
        );
    }
}