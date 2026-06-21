package org.northernarc.weeklyassignment.controller;

import org.northernarc.weeklyassignment.entity.LoanApplication;
import org.northernarc.weeklyassignment.service.LendingAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class LoanApplicationController {

    @Autowired
    private LendingAnalyticsService service;

    @PostMapping
    public ResponseEntity<LoanApplication> addApplication(@RequestBody LoanApplication application){
        return new ResponseEntity<>(service.addApplication(application),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllApplications(){
        return ResponseEntity.ok(service.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Optional<LoanApplication> application =  service.getApplicationById(id);
        if(application.isPresent()){
            return ResponseEntity.ok(application.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(@PathVariable String id,@RequestBody LoanApplication application){
        Optional<LoanApplication> existing = service.getApplicationById(id);
        if(existing.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
        }
        return ResponseEntity.ok(service.updateApplication(id,application));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable String id){
        boolean deleted = service.deleteApplication(id);
        if(deleted){
            return ResponseEntity.ok("Deleted Successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
    }

    //Custom Endpoints

    @PostMapping("/load")
    public ResponseEntity<String> loadApplications(
            @RequestBody List<String> records) {

        service.loadApplications(records);

        return ResponseEntity.ok("Applications loaded successfully");
    }

    @GetMapping("/top-credit-profiles")
    public ResponseEntity<List<LoanApplication>> topCreditProfiles(@RequestParam int n) {
        return ResponseEntity.ok(service.topCreditProfiles(n));
    }

    @GetMapping("/average-loan-amount-by-type")
    public ResponseEntity<?> averageLoanAmountByType() {
        return ResponseEntity.ok(service.averageLoanAmountByType());
    }

    @GetMapping("/highest-loan")
    public ResponseEntity<?> highestLoanApplication() {
        Optional<LoanApplication> application = service.highestLoanApplication();
        if (application.isPresent()) {
            return ResponseEntity.ok(application.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No applications found");
    }

    @GetMapping("/lenders/multiple-loan-types")
    public ResponseEntity<?> lendersWithMultipleLoanTypes() {
        return ResponseEntity.ok(service.lendersWithMultipleLoanTypes());
    }

    @GetMapping("/group-by-lender")
    public ResponseEntity<?> groupApplicationsByLender() {
        return ResponseEntity.ok(service.groupApplicationsByLender());
    }

    @GetMapping("/suspicious")
    public ResponseEntity<List<String>> suspiciousApplications() {
        return ResponseEntity.ok(service.suspiciousApplications());
    }

    @GetMapping("/loan-type-wise-top-applicant")
    public ResponseEntity<?> loanTypeWiseTopApplicantByLender() {
        return ResponseEntity.ok(service.loanTypeWiseTopApplicantByLender());
    }
}