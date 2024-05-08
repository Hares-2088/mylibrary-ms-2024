package com.cardealership.apigateway.businesslayer.loans;

import com.cardealership.apigateway.domainclientlayer.loans.LoanServiceClient;
import com.cardealership.apigateway.mapper.LoanResponseMapper;
import com.cardealership.apigateway.presentationlayer.loans.LoanRequestModel;
import com.cardealership.apigateway.presentationlayer.loans.LoanResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanServiceClient loanServiceClient;
    private final LoanResponseMapper loanResponseMapper;

    public LoanServiceImpl(LoanServiceClient loanServiceClient, LoanResponseMapper loanResponseMapper) {
        this.loanServiceClient = loanServiceClient;
        this.loanResponseMapper = loanResponseMapper;
    }

    @Override
    public List<LoanResponseModel> getAllLoans(String memberId) {
        return loanResponseMapper.responseModelListToResponseModelList(loanServiceClient.getAllLoans(memberId));
    }

    @Override
    public LoanResponseModel getLoanByLoanId(String loanId, String memberId) {
        return loanResponseMapper.responseModelToResponseModel(loanServiceClient.getLoan(loanId, memberId));
    }

    @Override
    public LoanResponseModel createLoan(LoanRequestModel loanRequestModel, String memberId) {
        return loanResponseMapper.responseModelToResponseModel(loanServiceClient.createLoan(loanRequestModel, memberId));
    }

    @Override
    public void updateLoan(String loanId, LoanRequestModel loanRequestModel, String memberId) {
        loanServiceClient.updateLoan(loanRequestModel, loanId, memberId);
    }

    @Override
    public void deleteLoan(String loanId, String memberId) {
        loanServiceClient.deleteLoan(loanId, memberId);
    }
}
