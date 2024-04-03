package com.mylibrary.members.utils.exceptions;

public class NotInRegionException extends RuntimeException{

    public NotInRegionException() {}

    public NotInRegionException(String message) { super(message); }

    public NotInRegionException(Throwable cause) { super(cause); }

    public NotInRegionException(String message, Throwable cause) { super(message, cause); }
}