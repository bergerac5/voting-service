package com.online.voting.voting.dtos;

import java.util.UUID;

public class PositionResponse {
    private UUID positionId;
    private String name;

    public PositionResponse() {
    }

    public PositionResponse(UUID positionId, String name) {
        this.positionId = positionId;
        this.name = name;
    }

    // Getters and setters
    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
