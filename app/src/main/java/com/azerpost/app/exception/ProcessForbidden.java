package com.azerpost.app.exception;

public class ProcessForbidden extends RuntimeException {
    public ProcessForbidden(String message) {
        super(message);
    }
}
