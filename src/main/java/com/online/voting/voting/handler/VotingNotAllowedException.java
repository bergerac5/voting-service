package com.online.voting.voting.handler;

public class VotingNotAllowedException extends RuntimeException {

    public VotingNotAllowedException(String message) {
        super(message);
    }

}
