package com.cardealership.apigateway.utils.exceptions;

public class InvalidPublicationYear extends RuntimeException{

    public InvalidPublicationYear() {}

    public InvalidPublicationYear(String message) { super(message); }

    public InvalidPublicationYear(Throwable cause) { super(cause); }

    public InvalidPublicationYear(String message, Throwable cause) { super(message, cause); }
}
