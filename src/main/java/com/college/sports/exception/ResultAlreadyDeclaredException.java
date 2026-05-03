package com.college.sports.exception;

public class ResultAlreadyDeclaredException extends RuntimeException {
    public ResultAlreadyDeclaredException(String message) {
        super(message);
    }
}
