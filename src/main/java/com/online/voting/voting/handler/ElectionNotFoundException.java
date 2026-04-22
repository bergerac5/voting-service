package com.online.voting.voting.handler;

public class ElectionNotFoundException extends RuntimeException {

    public ElectionNotFoundException(String message) {
        super(message);
    }
}