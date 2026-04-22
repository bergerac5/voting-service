package com.online.voting.voting.handler;

public class PositionNotFoundException extends RuntimeException {

    public PositionNotFoundException(String message) {
        super(message);
    }
}