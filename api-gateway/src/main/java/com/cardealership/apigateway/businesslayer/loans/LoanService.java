package com.cardealership.apigateway.businesslayer.loans;

import com.cardealership.apigateway.presentationlayer.loans.LoanRequestModel;
import com.cardealership.apigateway.presentationlayer.loans.LoanResponseModel;

import java.util.List;

public interface LoanService {

    public List<LoanResponseModel> getAllLoans(String memberId);
    public LoanResponseModel getLoanByLoanId(String loanId, String memberId);
    public LoanResponseModel createLoan(LoanRequestModel loanRequestModel, String memberId);
    public void updateLoan(String loanId, LoanRequestModel loanRequestModel, String memberId);
    public void deleteLoan(String loanId, String memberId);

}
