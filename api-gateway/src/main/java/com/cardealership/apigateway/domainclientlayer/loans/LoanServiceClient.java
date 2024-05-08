package com.cardealership.apigateway.domainclientlayer.loans;

import com.cardealership.apigateway.presentationlayer.loans.LoanRequestModel;
import com.cardealership.apigateway.presentationlayer.loans.LoanResponseModel;
import com.cardealership.apigateway.utils.exceptions.HttpErrorInfo;
import com.cardealership.apigateway.utils.exceptions.InvalidInputException;
import com.cardealership.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class LoanServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String LOAN_SERVICE_BASE_URL;

    private LoanServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                              @Value("${app.loans-service.host}") String loanServiceHost,
                              @Value("${app.loans-service.port}") String loanServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        LOAN_SERVICE_BASE_URL = "http://" + loanServiceHost + ":" + loanServicePort + "/api/v1/members";
    }

    public LoanResponseModel getLoan(String loanId, String memberId) {
        try{
            String url = LOAN_SERVICE_BASE_URL + "/" + memberId + "/loans/" + loanId;

            return restTemplate.getForObject(url, LoanResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw  handleHttpClientException(ex);
        }
    }

    public List<LoanResponseModel> getAllLoans(String memberId) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + memberId + "/loans";

            LoanResponseModel[] loanResponseModels = restTemplate.getForObject(url, LoanResponseModel[].class);
            assert loanResponseModels != null;
            return List.of(loanResponseModels);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public LoanResponseModel createLoan(LoanRequestModel loan, String memberId) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + memberId + "/loans";

            return restTemplate.postForObject(url, loan, LoanResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void updateLoan(LoanRequestModel loan, String loanId, String memberId) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + memberId + "/loans/" + loanId;
            restTemplate.put(url, loan);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteLoan(String loanId, String memberId) {
        try {
            String url = LOAN_SERVICE_BASE_URL + "/" + memberId + "/loans/" + loanId;

            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        //include all possible responses from the client
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}
