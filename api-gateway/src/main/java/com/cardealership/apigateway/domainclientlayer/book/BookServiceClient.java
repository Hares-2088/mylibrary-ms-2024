package com.cardealership.apigateway.domainclientlayer.book;

import com.cardealership.apigateway.presentationlayer.books.BookRequestModel;
import com.cardealership.apigateway.presentationlayer.books.BookResponseModel;
import com.cardealership.apigateway.utils.exceptions.HttpErrorInfo;
import com.cardealership.apigateway.utils.exceptions.InvalidInputException;
import com.cardealership.apigateway.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.io.IOException;
import java.util.List;

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

    public BookResponseModel getBookByBookId(String bookId) {
        try{
            String url = BOOK_SERVICE_BASE_URL + "/" + bookId;

            return restTemplate.getForObject(url, BookResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw  handleHttpClientException(ex);
        }
    }

    //Get all books
    public List<BookResponseModel> getAllBooks() {
        try {
            String url = BOOK_SERVICE_BASE_URL;

            BookResponseModel[] bookResponseModels = restTemplate.getForObject(url, BookResponseModel[].class);
            assert bookResponseModels != null;
            return List.of(bookResponseModels);


        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    //Create a book
    public BookResponseModel createBook(BookRequestModel bookRequestModel) {
        try {
            String url = BOOK_SERVICE_BASE_URL;

            return restTemplate.postForObject(url, bookRequestModel, BookResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    //Update a book
    public void updateBook(String bookId, BookRequestModel bookRequestModel) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + bookId;

            restTemplate.put(url, bookRequestModel);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    //Delete a book
    public void deleteBook(String bookId) {
        try {
            String url = BOOK_SERVICE_BASE_URL + "/" + bookId;

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
