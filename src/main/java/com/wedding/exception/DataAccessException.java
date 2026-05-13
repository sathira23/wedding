package com.wedding.exception;

/**
 * General runtime exception for persistence and database errors.
 * It allows the service layer to wrap lower-level JDBC exceptions
 * into a stable, application-specific exception type.
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
