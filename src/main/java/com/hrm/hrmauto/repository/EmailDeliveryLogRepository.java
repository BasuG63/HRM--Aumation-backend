package com.hrm.hrmauto.repository;

import com.hrm.hrmauto.entity.EmailDeliveryLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailDeliveryLogRepository
        extends JpaRepository<EmailDeliveryLog, Long> {

    List<EmailDeliveryLog>
    findByPayrollIdOrderBySentAtDesc(Long payrollId);

    List<EmailDeliveryLog>
    findAllByOrderBySentAtDesc();

    long countByStatus(String status);
}