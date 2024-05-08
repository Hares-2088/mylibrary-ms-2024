package com.cardealership.apigateway.utils.exceptions;

/**
 * @author Christine Gerard
 * @created 01/17/2024
 * @project contact-ws-2024
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException() {}

    public NotFoundException(String message) { super(message); }

    public NotFoundException(Throwable cause) { super(cause); }

    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}
