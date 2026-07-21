package org.northernarc.loanmanagementproject.controller;

import jakarta.validation.Valid;
import org.northernarc.loanmanagementproject.dto.DTOMapper;
import org.northernarc.loanmanagementproject.dto.request.LoanProductRequest;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.LoanProductResponse;
import org.northernarc.loanmanagementproject.dto.response.PagedData;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.repository.LoanProductRepository;
import org.northernarc.loanmanagementproject.service.LoanProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LoanProductController - Product catalogue.
 * All authenticated roles can browse; ADMIN manages, MANAGER may update.
 */
@RestController
@RequestMapping("/api/loan-products")
public class LoanProductController {

    private final LoanProductService loanProductService;
    private final LoanProductRepository loanProductRepository;
    private final DTOMapper dtoMapper;

    public LoanProductController(LoanProductService loanProductService,
                                LoanProductRepository loanProductRepository,
                                DTOMapper dtoMapper) {
        this.loanProductService = loanProductService;
        this.loanProductRepository = loanProductRepository;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LoanProductResponse>> create(@Valid @RequestBody LoanProductRequest request) {
        LoanProduct saved = loanProductService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Loan product created successfully", dtoMapper.toLoanProductResponse(saved)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoanProductResponse>>> getAll() {
        List<LoanProductResponse> products = loanProductService.getAll().stream()
                .map(dtoMapper::toLoanProductResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Loan products fetched successfully", products));
    }

    @GetMapping("/active")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<LoanProductResponse>>> getActive() {
        List<LoanProductResponse> products = loanProductService.getAllActive().stream()
                .map(dtoMapper::toLoanProductResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Active loan products fetched successfully", products));
    }

    @GetMapping("/{loanCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<LoanProductResponse>> getByCode(@PathVariable String loanCode) {
        LoanProduct product = loanProductService.getByCode(loanCode);
        return ResponseEntity.ok(ApiResponse.success("Loan product fetched successfully",
                dtoMapper.toLoanProductResponse(product)));
    }

    @GetMapping("/paged")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PagedData<LoanProductResponse>>> getPaged(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "dailyPenaltyRate") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LoanProduct> products = loanProductRepository.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success("Loan products fetched successfully", toPagedData(products)));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PagedData<LoanProductResponse>>> search(
            @RequestParam(value = "loanType", required = false) String loanType,
            @RequestParam(value = "loanName", required = false) String loanName,
            @RequestParam(value = "minPenalty", required = false) Double minPenalty,
            @RequestParam(value = "maxPenalty", required = false) Double maxPenalty,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "dailyPenaltyRate") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LoanProduct> products;

        if (loanType != null && !loanType.isEmpty()) {
            products = loanProductRepository.findByLoanType(loanType, pageable);
        } else if (loanName != null && !loanName.isEmpty()) {
            products = loanProductRepository.findByLoanNameContainingIgnoreCase(loanName, pageable);
        } else if (minPenalty != null && maxPenalty != null) {
            products = loanProductRepository.findByDailyPenaltyRateBetween(minPenalty, maxPenalty, pageable);
        } else if (minPenalty != null) {
            products = loanProductRepository.findByDailyPenaltyRateGreaterThan(minPenalty, pageable);
        } else {
            products = loanProductRepository.findAll(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success("Loan products fetched successfully", toPagedData(products)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<ApiResponse<LoanProductResponse>> update(@Valid @RequestBody LoanProductRequest request) {
        LoanProduct updated = loanProductService.update(request);
        return ResponseEntity.ok(ApiResponse.success("Loan product updated successfully",
                dtoMapper.toLoanProductResponse(updated)));
    }

    @DeleteMapping("/{loanCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String loanCode) {
        loanProductService.delete(loanCode);
        return ResponseEntity.ok(ApiResponse.success("Loan product deleted successfully"));
    }

    private PagedData<LoanProductResponse> toPagedData(Page<LoanProduct> page) {
        List<LoanProductResponse> items = page.getContent().stream()
                .map(dtoMapper::toLoanProductResponse)
                .toList();
        return PagedData.<LoanProductResponse>builder()
                .items(items)
                .page(PagedData.PageMeta.builder()
                        .number(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .build())
                .build();
    }
}
