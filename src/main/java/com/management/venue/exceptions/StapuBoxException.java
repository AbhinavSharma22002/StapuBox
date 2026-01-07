package com.management.venue.exceptions;

public class StapuBoxException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    // We store the cause as a general Throwable internally
    private final Throwable specificCause;
    private final String errorCode;

    public <T extends Throwable> StapuBoxException(String message, String errorCode, T cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.specificCause = cause;
    }

    @SuppressWarnings("unchecked")
    public <T extends Throwable> T getSpecificCause() {
        return (T) specificCause;
    }

    public String getErrorCode() {
        return errorCode;
    }
}