package com.cardealership.apigateway.domainclientlayer.member;

import com.cardealership.apigateway.presentationlayer.members.MemberRequestModel;
import com.cardealership.apigateway.presentationlayer.members.MemberResponseModel;
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
public class MemberServiceClient {


    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final String MEMBER_SERVICE_BASE_URL;

    private MemberServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                              @Value("${app.members-service.host}") String memberServiceHost,
                              @Value("${app.members-service.port}") String memberServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        MEMBER_SERVICE_BASE_URL = "http://" + memberServiceHost + ":" + memberServicePort + "/api/v1/members";
    }

    public MemberResponseModel getMemberByMemberId(String memberId) {
        try{
            String url = MEMBER_SERVICE_BASE_URL + "/" + memberId;

            return restTemplate.getForObject(url, MemberResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw  handleHttpClientException(ex);
        }
    }

    //get all members
    public List<MemberResponseModel> getAllMembers() {
        try{
            String url = MEMBER_SERVICE_BASE_URL;

            MemberResponseModel[] memberResponseModels = restTemplate.getForObject(url, MemberResponseModel[].class);
            assert memberResponseModels != null;
            return List.of(memberResponseModels);

        } catch (HttpClientErrorException ex) {
            throw  handleHttpClientException(ex);
        }
    }

    //create a member
    public MemberResponseModel createMember(MemberRequestModel memberRequestModel) {
        try {
            String url = MEMBER_SERVICE_BASE_URL;

            return restTemplate.postForObject(url, memberRequestModel, MemberResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    //update a member
    public void updateMember(String memberId, MemberRequestModel memberRequestModel) {
        try {
            String url = MEMBER_SERVICE_BASE_URL + "/" + memberId;

            restTemplate.put(url, memberRequestModel);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    //delete a member
    public void deleteMember(String memberId) {
        try {
            String url = MEMBER_SERVICE_BASE_URL + "/" + memberId;

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
