package com.hrm.hrmauto.service;

import com.hrm.hrmauto.dto.LeaveBalanceResponse;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.LeaveBalance;
import com.hrm.hrmauto.repository.EmployeeRepository;
import com.hrm.hrmauto.repository.LeaveBalanceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
public class LeaveBalanceService {

    // =====================================================
    // DEFAULT YEARLY LEAVE
    // =====================================================

    private static final int DEFAULT_CL = 12;
    private static final int DEFAULT_SL = 12;
    private static final int DEFAULT_EL = 15;


    private final EmployeeRepository employeeRepository;

    private final LeaveBalanceRepository leaveBalanceRepository;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LeaveBalanceService(
            LeaveBalanceRepository leaveBalanceRepository,
            EmployeeRepository employeeRepository) {

        this.leaveBalanceRepository =
                leaveBalanceRepository;

        this.employeeRepository =
                employeeRepository;
    }


    // =====================================================
    // CREATE INITIAL BALANCE
    // Called when employee is created
    // =====================================================

    @Transactional
    public LeaveBalance createInitialBalance(
            Employee employee) {

        int currentYear =
                Year.now().getValue();

        return createBalanceIfNotExists(
                employee,
                currentYear
        );
    }


    // =====================================================
    // CREATE BALANCE IF NOT EXISTS
    // =====================================================

    @Transactional
    public LeaveBalance createBalanceIfNotExists(
            Employee employee,
            int year) {

        return leaveBalanceRepository
                .findByEmployeeIdAndYear(
                        employee.getId(),
                        year
                )
                .orElseGet(() -> {

                    LeaveBalance balance =
                            new LeaveBalance();

                    balance.setEmployee(employee);

                    balance.setYear(year);

                    balance.setCasualLeave(
                            DEFAULT_CL
                    );

                    balance.setSickLeave(
                            DEFAULT_SL
                    );

                    balance.setEarnedLeave(
                            DEFAULT_EL
                    );

                    return leaveBalanceRepository.save(
                            balance
                    );
                });
    }


    // =====================================================
    // GET BALANCE BY EMPLOYEE ID
    // =====================================================

    @Transactional
    public LeaveBalanceResponse getEmployeeLeaveBalance(
            Long employeeId) {

        Employee employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with ID: "
                                                + employeeId
                                )
                        );

        int currentYear =
                Year.now().getValue();


        // Automatically create balance if missing
        LeaveBalance balance =
                createBalanceIfNotExists(
                        employee,
                        currentYear
                );


        return convertToResponse(
                balance
        );
    }


    // =====================================================
    // GET BALANCE BY LOGGED-IN EMPLOYEE EMAIL
    //
    // THIS METHOD IS USED BY:
    //
    // GET /api/employee/leave/balance
    //
    // =====================================================

    @Transactional
    public LeaveBalanceResponse getEmployeeLeaveBalanceByEmail(
            String email) {

        if (email == null ||
                email.isBlank()) {

            throw new RuntimeException(
                    "Employee email is required"
            );
        }


        // -------------------------------------------------
        // FIND EMPLOYEE USING JWT EMAIL
        // -------------------------------------------------

        Employee employee =
                employeeRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with email: "
                                                + email
                                )
                        );


        // -------------------------------------------------
        // CURRENT YEAR
        // -------------------------------------------------

        int currentYear =
                Year.now().getValue();


        // -------------------------------------------------
        // GET OR CREATE BALANCE
        //
        // This prevents 500 when an old employee
        // does not yet have a balance record.
        // -------------------------------------------------

        LeaveBalance balance =
                createBalanceIfNotExists(
                        employee,
                        currentYear
                );


        // -------------------------------------------------
        // RETURN RESPONSE
        // -------------------------------------------------

        return convertToResponse(
                balance
        );
    }


    // =====================================================
    // RENEW BALANCE FOR NEW YEAR
    // =====================================================

    @Transactional
    public void renewLeaveBalancesForYear(
            int year) {

        List<Employee> employees =
                employeeRepository.findAll();


        for (Employee employee : employees) {

            LeaveBalance balance =
                    createBalanceIfNotExists(
                            employee,
                            year
                    );


            System.out.println(
                    "Leave balance ready for "
                            + employee.getEmployeeCode()
                            + " - "
                            + year
            );
        }
    }


    // =====================================================
    // CONVERT ENTITY -> RESPONSE
    // =====================================================

    private LeaveBalanceResponse convertToResponse(
            LeaveBalance balance) {

        return new LeaveBalanceResponse(

                balance.getEmployee().getId(),

                balance.getEmployee()
                        .getEmployeeCode(),

                balance.getEmployee()
                        .getName(),

                balance.getYear(),

                balance.getCasualLeave(),

                balance.getSickLeave(),

                balance.getEarnedLeave()
        );
    }
}