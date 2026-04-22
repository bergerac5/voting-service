package com.online.voting.voting.dtos;

import java.util.UUID;

public class ElectionResponse {

    private UUID electionId;
    private String title;

    public ElectionResponse() {
    }

    public ElectionResponse(UUID electionId, String title) {
        this.electionId = electionId;
        this.title = title;
    }

    // Getters and setters
    public UUID getElectionId() {
        return electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
