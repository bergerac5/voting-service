package com.online.voting.voting.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class CastVoteResponse {

    private UUID voteId;
    private UUID voterId;
    private UUID candidateId;
    private UUID electionId;
    private UUID positionId;
    private LocalDateTime votedAt;
    private String electionTitle;
    private String positionName;
    private String candidateName;
    private String message;

    public CastVoteResponse() {
    }

    public CastVoteResponse(UUID voteId, String electionTitle, String positionName, String candidateName,
            String message) {
        this.voteId = voteId;
        this.electionTitle = electionTitle;
        this.positionName = positionName;
        this.candidateName = candidateName;
        this.message = message;
    }

    public UUID getVoteId() {
        return voteId;
    }

    public void setVoteId(UUID voteId) {
        this.voteId = voteId;
    }

    public UUID getVoterId() {
        return voterId;
    }

    public void setVoterId(UUID voterId) {
        this.voterId = voterId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
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

    public LocalDateTime getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(LocalDateTime votedAt) {
        this.votedAt = votedAt;
    }

    public String getElectionTitle() {
        return electionTitle;
    }

    public void setElectionTitle(String electionTitle) {
        this.electionTitle = electionTitle;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getMessage() {
        return message;
    }

}
