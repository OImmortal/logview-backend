package com.logview.logview.domain.exception;

public class LogFileNotFoundException extends RuntimeException {

    public LogFileNotFoundException(String fileId) {
        super("Log file not found: " + fileId);
    }
}
