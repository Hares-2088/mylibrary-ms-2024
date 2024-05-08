package com.mylibrary.loans.domainclientlayer.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mylibrary.loans.utils.exceptions.HttpErrorInfo;
import com.mylibrary.loans.utils.exceptions.InvalidInputException;
import com.mylibrary.loans.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class ReservationServiceClient {
    
    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final String RESERVATION_SERVICE_BASE_URL;

    private ReservationServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                     @Value("${app.reservations-service.host}") String reservationServiceHost,
                                     @Value("${app.reservations-service.port}") String reservationServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        RESERVATION_SERVICE_BASE_URL = "http://" + reservationServiceHost + ":" + reservationServicePort + "/api/v1/members/";
    }

    public ReservationModel getReservationByReservationId(String reservationId, String memberId) {
        try{
            String url = RESERVATION_SERVICE_BASE_URL + memberId + "/reservations/" + reservationId;

            return restTemplate.getForObject(url, ReservationModel.class);
        } catch (HttpClientErrorException ex) {
            throw  handleHttpClientException(ex);
        }
    }

    public List<ReservationModel> getAllReservations(String memberId) {
        try {
            String url = RESERVATION_SERVICE_BASE_URL + memberId + "/reservations";

            ReservationModel[] reservationModels = restTemplate.getForObject(url, ReservationModel[].class);
            assert reservationModels != null;
            return List.of(reservationModels);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public ReservationModel createReservation(ReservationModel reservation, String memberId) {
        try {
            String url = RESERVATION_SERVICE_BASE_URL + memberId + "/reservations";

            return restTemplate.postForObject(url, reservation, ReservationModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void updateReservation(ReservationModel reservation, String memberId) {
        try {
            String url = RESERVATION_SERVICE_BASE_URL + memberId + "/reservations/" + reservation.getReservationId();

            restTemplate.put(url, reservation);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteReservation(String reservationId, String memberId) {
        try {
            String url = RESERVATION_SERVICE_BASE_URL + memberId + "/reservations/" +  reservationId;

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
