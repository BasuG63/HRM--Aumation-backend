package com.hrm.hrmauto.service;

import com.hrm.hrmauto.dto.SalaryStructureRequest;
import com.hrm.hrmauto.dto.SalaryStructureResponse;
import com.hrm.hrmauto.entity.SalaryStructure;
import com.hrm.hrmauto.repository.SalaryStructureRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SalaryStructureService {

    private final SalaryStructureRepository repository;

    public SalaryStructureService(
            SalaryStructureRepository repository) {

        this.repository = repository;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public SalaryStructureResponse create(
            SalaryStructureRequest request) {

        validateRequest(request);

        SalaryStructure salary =
                new SalaryStructure();

        salary.setStructureName(
                request.getStructureName().trim()
        );

        salary.setBasicSalary(
                request.getBasicSalary()
        );

        salary.setHra(
                request.getHra()
        );

        salary.setSpecialAllowance(
                request.getSpecialAllowance()
        );

        // =================================================
        // CALCULATE GROSS SALARY
        // =================================================

        BigDecimal grossSalary =
                request.getBasicSalary()
                        .add(request.getHra())
                        .add(request.getSpecialAllowance());

        salary.setGrossSalary(
                grossSalary
        );

        salary.setPfAmount(
                request.getPfAmount()
        );

        salary.setEsiAmount(
                request.getEsiAmount()
        );

        salary.setProfessionalTax(
                request.getProfessionalTax()
        );

        salary.setEffectiveFrom(
                request.getEffectiveFrom()
        );

        salary.setEffectiveTo(
                request.getEffectiveTo()
        );

        salary.setActive(true);

        SalaryStructure saved =
                repository.save(salary);

        return convertToResponse(saved);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    public List<SalaryStructureResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // GET ACTIVE
    // =====================================================

    public List<SalaryStructureResponse> getActive() {

        return repository.findByActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    public SalaryStructureResponse getById(
            Long id) {

        SalaryStructure salary =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary structure not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(salary);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public SalaryStructureResponse update(
            Long id,
            SalaryStructureRequest request) {

        validateRequest(request);

        SalaryStructure salary =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary structure not found with id: "
                                                + id
                                )
                        );

        salary.setStructureName(
                request.getStructureName().trim()
        );

        salary.setBasicSalary(
                request.getBasicSalary()
        );

        salary.setHra(
                request.getHra()
        );

        salary.setSpecialAllowance(
                request.getSpecialAllowance()
        );

        // Recalculate gross

        BigDecimal grossSalary =
                request.getBasicSalary()
                        .add(request.getHra())
                        .add(request.getSpecialAllowance());

        salary.setGrossSalary(
                grossSalary
        );

        salary.setPfAmount(
                request.getPfAmount()
        );

        salary.setEsiAmount(
                request.getEsiAmount()
        );

        salary.setProfessionalTax(
                request.getProfessionalTax()
        );

        salary.setEffectiveFrom(
                request.getEffectiveFrom()
        );

        salary.setEffectiveTo(
                request.getEffectiveTo()
        );

        SalaryStructure updated =
                repository.save(salary);

        return convertToResponse(updated);
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Transactional
    public void delete(Long id) {

        SalaryStructure salary =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary structure not found with id: "
                                                + id
                                )
                        );

        repository.delete(salary);
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateRequest(
            SalaryStructureRequest request) {

        if (request.getStructureName() == null ||
                request.getStructureName()
                        .trim()
                        .isEmpty()) {

            throw new RuntimeException(
                    "Structure name is required"
            );
        }

        if (request.getBasicSalary() == null ||
                request.getBasicSalary()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Basic salary cannot be negative"
            );
        }

        if (request.getHra() == null ||
                request.getHra()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "HRA cannot be negative"
            );
        }

        if (request.getSpecialAllowance() == null ||
                request.getSpecialAllowance()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Special allowance cannot be negative"
            );
        }

        if (request.getPfAmount() == null ||
                request.getPfAmount()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "PF amount cannot be negative"
            );
        }

        if (request.getEsiAmount() == null ||
                request.getEsiAmount()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "ESI amount cannot be negative"
            );
        }

        if (request.getProfessionalTax() == null ||
                request.getProfessionalTax()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Professional tax cannot be negative"
            );
        }

        if (request.getEffectiveFrom() == null) {

            throw new RuntimeException(
                    "Effective from date is required"
            );
        }

        if (request.getEffectiveTo() != null &&
                request.getEffectiveTo()
                        .isBefore(
                                request.getEffectiveFrom()
                        )) {

            throw new RuntimeException(
                    "Effective to date cannot be before effective from date"
            );
        }
    }

    // =====================================================
    // CONVERT ENTITY -> RESPONSE
    // =====================================================

    private SalaryStructureResponse convertToResponse(
            SalaryStructure salary) {

        BigDecimal totalDeductions =
                salary.getPfAmount()
                        .add(salary.getEsiAmount())
                        .add(salary.getProfessionalTax());

        BigDecimal estimatedNetSalary =
                salary.getGrossSalary()
                        .subtract(totalDeductions);

        return new SalaryStructureResponse(
                salary.getId(),
                salary.getStructureName(),
                salary.getBasicSalary(),
                salary.getHra(),
                salary.getSpecialAllowance(),
                salary.getGrossSalary(),
                salary.getPfAmount(),
                salary.getEsiAmount(),
                salary.getProfessionalTax(),
                totalDeductions,
                estimatedNetSalary,
                salary.getEffectiveFrom(),
                salary.getEffectiveTo(),
                salary.isActive()
        );
    }
}