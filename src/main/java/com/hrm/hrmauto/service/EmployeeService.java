package com.hrm.hrmauto.service;

import com.hrm.hrmauto.dto.EmployeeRequest;
import com.hrm.hrmauto.dto.EmployeeResponse;
import com.hrm.hrmauto.entity.Employee;
import com.hrm.hrmauto.entity.Role;
import com.hrm.hrmauto.entity.User;
import com.hrm.hrmauto.repository.EmployeeRepository;
import com.hrm.hrmauto.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final LeaveBalanceService leaveBalanceService;
    private final EmployeeEmailService employeeEmailService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmployeeService(
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder,
            LeaveBalanceService leaveBalanceService,
            EmployeeEmailService employeeEmailService) {

        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.leaveBalanceService = leaveBalanceService;
        this.employeeEmailService = employeeEmailService;
    }

    // =====================================================
    // GET EMPLOYEE BY EMPLOYEE CODE
    // =====================================================

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeByEmployeeCode(
            String employeeCode) {

        Employee employee =
                employeeRepository
                        .findByEmployeeCode(employeeCode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with employee code: "
                                                + employeeCode
                                )
                        );

        return convertToResponse(
                employee,
                null
        );
    }

    // =====================================================
    // CREATE EMPLOYEE
    // =====================================================

    @Transactional
    public EmployeeResponse createEmployee(
            EmployeeRequest request) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "CREATE EMPLOYEE STARTED"
        );

        // =================================================
        // 1. CHECK EMAIL
        // =================================================

        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "User already exists with email: "
                            + request.getEmail()
            );
        }

        // =================================================
        // 2. CHECK PHONE
        // =================================================

        if (employeeRepository.existsByPhone(
                request.getPhone())) {

            throw new RuntimeException(
                    "Employee already exists with phone number: "
                            + request.getPhone()
            );
        }

        // =================================================
        // 3. GENERATE AUTOMATIC PASSWORD
        // =================================================

        String generatedPassword =
                generateEmployeePassword(
                        request.getName(),
                        request.getPhone()
                );

        System.out.println(
                "PASSWORD GENERATED"
        );

        // =================================================
        // 4. CREATE USER
        // =================================================

        User user = new User();

        user.setEmail(
                request.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        generatedPassword
                )
        );

        user.setRole(
                Role.EMPLOYEE
        );

        User savedUser =
                userRepository.save(user);

        System.out.println(
                "USER CREATED: "
                        + savedUser.getId()
        );

        // =================================================
        // 5. CREATE EMPLOYEE
        // =================================================

        Employee employee = new Employee();

        employee.setName(
                request.getName()
        );

        employee.setEmail(
                request.getEmail()
        );

        employee.setPhone(
                request.getPhone()
        );

        employee.setDepartment(
                request.getDepartment()
        );

        employee.setDesignation(
                request.getDesignation()
        );

        employee.setJoiningDate(
                request.getJoiningDate()
        );

        // =================================================
        // 6. GENERATE EMPLOYEE CODE
        // =================================================

        String employeeCode =
                generateEmployeeCode();

        employee.setEmployeeCode(
                employeeCode
        );

        // =================================================
        // 7. CONNECT EMPLOYEE WITH USER
        // =================================================

        employee.setUser(
                savedUser
        );

        // =================================================
        // 8. SAVE EMPLOYEE
        // =================================================

        Employee savedEmployee =
                employeeRepository.save(
                        employee
                );

        System.out.println(
                "EMPLOYEE CREATED: "
                        + savedEmployee.getId()
        );

        System.out.println(
                "EMPLOYEE CODE: "
                        + savedEmployee.getEmployeeCode()
        );

        // =================================================
        // 9. CREATE INITIAL LEAVE BALANCE
        // =================================================

        leaveBalanceService.createInitialBalance(
                savedEmployee
        );

        System.out.println(
                "LEAVE BALANCE CREATED"
        );

        // =================================================
        // 10. START ASYNC WELCOME EMAIL
        // =================================================

        System.out.println(
                "STARTING ASYNC WELCOME EMAIL"
        );

        /*
         * IMPORTANT:
         *
         * EmployeeEmailService.sendEmployeeWelcomeEmail()
         * must contain @Async.
         *
         * EmployeeService itself does NOT need @Async.
         */

        employeeEmailService.sendEmployeeWelcomeEmail(
                savedEmployee,
                generatedPassword
        );

        System.out.println(
                "ASYNC EMAIL REQUEST SUBMITTED"
        );

        // =================================================
        // 11. RETURN RESPONSE
        // =================================================

        EmployeeResponse response =
                convertToResponse(
                        savedEmployee,
                        "Employee created successfully"
                );

        System.out.println(
                "RETURNING EMPLOYEE RESPONSE"
        );

        System.out.println(
                "========================================"
        );

        return response;
    }

    // =====================================================
    // GET ALL EMPLOYEES
    // =====================================================

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository
                .findAll()
                .stream()
                .map(employee ->
                        convertToResponse(
                                employee,
                                null
                        )
                )
                .collect(Collectors.toList());
    }

    // =====================================================
    // GET EMPLOYEE BY DATABASE ID
    // =====================================================

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(
            Long id) {

        Employee employee =
                employeeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with ID: "
                                                + id
                                )
                        );

        return convertToResponse(
                employee,
                null
        );
    }

    // =====================================================
    // GET EMPLOYEE BY PHONE
    // =====================================================

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeByPhone(
            String phone) {

        Employee employee =
                employeeRepository
                        .findByPhone(phone)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with phone: "
                                                + phone
                                )
                        );

        return convertToResponse(
                employee,
                null
        );
    }

    // =====================================================
    // CONVERT ENTITY -> RESPONSE
    // =====================================================

    private EmployeeResponse convertToResponse(
            Employee employee,
            String message) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getJoiningDate(),
                message
        );
    }

    // =====================================================
    // AUTOMATIC PASSWORD GENERATOR
    // =====================================================

    private String generateEmployeePassword(
            String name,
            String phone) {

        if (name == null ||
                name.trim().isEmpty()) {

            throw new RuntimeException(
                    "Employee name is required"
            );
        }

        if (phone == null ||
                phone.trim().isEmpty()) {

            throw new RuntimeException(
                    "Employee phone number is required"
            );
        }

        String cleanName =
                name.trim()
                        .replaceAll("\\s+", "");

        String cleanPhone =
                phone.replaceAll("\\D", "");

        if (cleanPhone.length() < 4) {

            throw new RuntimeException(
                    "Phone number must contain at least 4 digits"
            );
        }

        String namePart =
                cleanName.substring(
                        0,
                        Math.min(
                                4,
                                cleanName.length()
                        )
                )
                .toUpperCase();

        String phonePart =
                cleanPhone.substring(
                        cleanPhone.length() - 4
                );

        return namePart + phonePart;
    }

    // =====================================================
    // EMPLOYEE CODE GENERATOR
    // =====================================================

    private String generateEmployeeCode() {

        Long maxNumber =
                employeeRepository.findMaxEmployeeCodeNumber();

        return "FTC" + (maxNumber + 1);
    }
}