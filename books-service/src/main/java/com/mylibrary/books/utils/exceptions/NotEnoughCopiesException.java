package com.mylibrary.books.utils.exceptions;

public class NotEnoughCopiesException extends RuntimeException{

    public NotEnoughCopiesException() {}

    public NotEnoughCopiesException(String message) { super(message); }

    public NotEnoughCopiesException(Throwable cause) { super(cause); }

    public NotEnoughCopiesException(String message, Throwable cause) { super(message, cause); }
}
