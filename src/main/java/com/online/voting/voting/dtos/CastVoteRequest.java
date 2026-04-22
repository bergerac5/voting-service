package com.online.voting.voting.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for casting a vote.
 * 
 * All fields are required and must be valid UUIDs.
 */
public class CastVoteRequest {

    @NotNull(message = "Voter ID is required")
    private UUID voterId;
    
    @NotNull(message = "Election ID is required")
    private UUID electionId;
    
    @NotNull(message = "Position ID is required")
    private UUID positionId;
    
    @NotNull(message = "Candidate ID is required")
    private UUID candidateId;

    public UUID getVoterId() {
        return voterId;
    }

    public void setVoterId(UUID voterId) {
        this.voterId = voterId;
    }

    public UUID getElectionId() {
        return electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }
}
