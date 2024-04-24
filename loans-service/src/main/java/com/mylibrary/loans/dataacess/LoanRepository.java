package com.mylibrary.loans.dataacess;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoanRepository extends MongoRepository<Loan,String> {

    Loan findLoanByLoanIdentifier_LoanIdAndMemberModel_MemberId(String loanId, String memberId);

    List<Loan> findAllByMemberModel_MemberId(String memberId);

}
