package com.mylibrary.utils;

import com.mylibrary.utils.exceptions.HttpErrorInfo;
import com.mylibrary.utils.exceptions.InvalidPublicationYear;
import com.mylibrary.utils.exceptions.NotEnoughCopiesException;
import com.mylibrary.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

/**
 * @author Christine Gerard
 * @created 01/17/2024
 * @project contact-ws-2024
 */

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {


    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public HttpErrorInfo handleNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotEnoughCopiesException.class)
    public HttpErrorInfo handleNotEnoughCopiesException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidPublicationYear.class)
    public HttpErrorInfo handleInvalidPublicationYear(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = request.getDescription(false);
        // final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();
        log.debug("message is: " + message);

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return new HttpErrorInfo(httpStatus, path, message);
    }
}