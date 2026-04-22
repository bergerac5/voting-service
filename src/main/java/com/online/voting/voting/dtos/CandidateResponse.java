package com.online.voting.voting.dtos;

import java.util.UUID;

public class CandidateResponse {

    private UUID candidateId;
    private String firstName;
    private String lastName;
    private String username;

    public CandidateResponse() {
    }

    public CandidateResponse(UUID candidateId, String firstName, String lastName, String username) {
        this.candidateId = candidateId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public UUID getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
