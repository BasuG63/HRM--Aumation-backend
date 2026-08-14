package com.hrm.hrmauto.repository;

import com.hrm.hrmauto.entity.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryStructureRepository
        extends JpaRepository<SalaryStructure, Long> {

    List<SalaryStructure> findByActiveTrue();

}