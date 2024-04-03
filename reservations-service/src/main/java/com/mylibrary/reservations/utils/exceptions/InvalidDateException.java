package com.mylibrary.reservations.utils.exceptions;

public class InvalidDateException extends RuntimeException{

    public InvalidDateException() {}

    public InvalidDateException(String message) { super(message); }

    public InvalidDateException(Throwable cause) { super(cause); }

    public InvalidDateException(String message, Throwable cause) { super(message, cause); }
}
