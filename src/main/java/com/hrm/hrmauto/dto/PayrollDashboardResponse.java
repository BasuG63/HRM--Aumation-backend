package com.hrm.hrmauto.dto;

public class PayrollDashboardResponse {

    private long totalEmployeesProcessed;

    private long pendingPayroll;

    private long payslipsSentSuccessfully;

    private long failedEmailDeliveries;

    private long totalCasualLeave;

    private long totalSickLeave;

    private long totalEarnedLeave;

    private long casualLeaveUsed;

    private long sickLeaveUsed;

    private long earnedLeaveUsed;

    public PayrollDashboardResponse() {
    }

    public PayrollDashboardResponse(
            long totalEmployeesProcessed,
            long pendingPayroll,
            long payslipsSentSuccessfully,
            long failedEmailDeliveries,
            long totalCasualLeave,
            long totalSickLeave,
            long totalEarnedLeave,
            long casualLeaveUsed,
            long sickLeaveUsed,
            long earnedLeaveUsed) {

        this.totalEmployeesProcessed =
                totalEmployeesProcessed;

        this.pendingPayroll =
                pendingPayroll;

        this.payslipsSentSuccessfully =
                payslipsSentSuccessfully;

        this.failedEmailDeliveries =
                failedEmailDeliveries;

        this.totalCasualLeave =
                totalCasualLeave;

        this.totalSickLeave =
                totalSickLeave;

        this.totalEarnedLeave =
                totalEarnedLeave;

        this.casualLeaveUsed =
                casualLeaveUsed;

        this.sickLeaveUsed =
                sickLeaveUsed;

        this.earnedLeaveUsed =
                earnedLeaveUsed;
    }

    public long getTotalEmployeesProcessed() {
        return totalEmployeesProcessed;
    }

    public long getPendingPayroll() {
        return pendingPayroll;
    }

    public long getPayslipsSentSuccessfully() {
        return payslipsSentSuccessfully;
    }

    public long getFailedEmailDeliveries() {
        return failedEmailDeliveries;
    }

    public long getTotalCasualLeave() {
        return totalCasualLeave;
    }

    public long getTotalSickLeave() {
        return totalSickLeave;
    }

    public long getTotalEarnedLeave() {
        return totalEarnedLeave;
    }

    public long getCasualLeaveUsed() {
        return casualLeaveUsed;
    }

    public long getSickLeaveUsed() {
        return sickLeaveUsed;
    }

    public long getEarnedLeaveUsed() {
        return earnedLeaveUsed;
    }
}