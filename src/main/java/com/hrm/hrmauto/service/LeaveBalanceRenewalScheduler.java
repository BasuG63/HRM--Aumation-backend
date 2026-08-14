package com.hrm.hrmauto.service;
import com.hrm.hrmauto.dto.LeaveBalanceResponse;
import com.hrm.hrmauto.entity.LeaveBalance;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class LeaveBalanceRenewalScheduler {

    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceRenewalScheduler(
            LeaveBalanceService leaveBalanceService) {

        this.leaveBalanceService =
                leaveBalanceService;
    }

    // Runs every January 1 at 00:05 AM
    @Scheduled(
        cron = "0 5 0 1 1 *",
        zone = "Asia/Kolkata"
    )
    public void renewAnnualLeave() {

        int newYear =
                Year.now().getValue();

        System.out.println(
                "Starting annual leave renewal for "
                        + newYear
        );

        leaveBalanceService
                .renewLeaveBalancesForYear(
                        newYear
                );

        System.out.println(
                "Annual leave renewal completed for "
                        + newYear
        );
    }
}