package com.mylibrary.reservations.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylibrary.reservations.utils.exceptions.HttpErrorInfo;
import com.mylibrary.reservations.utils.exceptions.InvalidInputException;
import com.mylibrary.reservations.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class BookServiceClient {

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final String BOOK_SERVICE_BASE_URL;

    private BookServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                              @Value("${app.books-service.host}") String bookServiceHost,
                              @Value("${app.books-service.port}") String bookServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        BOOK_SERVICE_BASE_URL = "http://" + bookServiceHost + ":" + bookServicePort + "/api/v1/books";
    }

    public BookModel getBookByBookId(String bookId) {
        try{
            String url = BOOK_SERVICE_BASE_URL + "/" + bookId;

            return restTemplate.getForObject(url, BookModel.class);
        } catch (HttpClientErrorException ex) {
            throw  handleHttpClientException(ex);
        }
    }

    public void reduceBookAvailability(String bookId) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + bookId + "/reduceAvailability";

            restTemplate.patchForObject(url, null, BookModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void increaseBookAvailability(String bookId) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + bookId + "/increaseAvailability";

            restTemplate.patchForObject(url, null, BookModel.class);
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
