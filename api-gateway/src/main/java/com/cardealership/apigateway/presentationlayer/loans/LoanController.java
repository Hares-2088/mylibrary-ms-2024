package com.cardealership.apigateway.presentationlayer.loans;

import com.cardealership.apigateway.businesslayer.loans.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members/{memberId}/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<LoanResponseModel>> getLoans(@PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(loanService.getAllLoans(memberId));
    }

    @GetMapping(value = "/{loanId}", produces = "application/json")
    public ResponseEntity<LoanResponseModel> getLoan(@PathVariable String loanId, @PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(loanService.getLoanByLoanId(loanId, memberId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<LoanResponseModel> createLoan(@RequestBody LoanRequestModel loanRequestModel, @PathVariable String memberId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoan(loanRequestModel, memberId));
    }

    @PutMapping(value = "/{loanId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> updateLoan(@RequestBody LoanRequestModel loanRequestModel, @PathVariable String loanId, @PathVariable String memberId) {
        loanService.updateLoan(loanId, loanRequestModel, memberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable String memberId, @PathVariable String loanId) {
        loanService.deleteLoan(loanId, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
