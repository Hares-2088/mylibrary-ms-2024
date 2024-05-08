package com.mylibrary.loans.business;



import com.mylibrary.loans.presentation.LoanRequestModel;
import com.mylibrary.loans.presentation.LoanResponseModel;

import java.util.List;

public interface LoanService {
    List<LoanResponseModel> getLoans(String memberId);
    LoanResponseModel getLoan(String loanId,  String memberId);
    LoanResponseModel createLoan(LoanRequestModel loanRequestModel, String memberId);
    LoanResponseModel updateLoan(LoanRequestModel loanRequestModel, String loanId, String memberId);
    void deleteLoan(String loanId, String memberId);
    void deleteLoans(String memberId);

}
