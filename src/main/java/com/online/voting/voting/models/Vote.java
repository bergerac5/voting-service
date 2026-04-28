package com.online.voting.voting.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "votes", indexes = {
        @Index(name = "idx_voter_position", columnList = "voter_id, position_id"),
        @Index(name = "idx_position", columnList = "position_id"),
        @Index(name = "idx_election", columnList = "election_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_voter_position", columnNames = { "voter_id", "position_id" })
})
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID voteId;

    @Column(name = "voter_id", nullable = false, updatable = false)
    private UUID voterId;

    @Column(name = "election_id", nullable = false, updatable = false)
    private UUID electionId;

    @Column(name = "position_id", nullable = false, updatable = false)
    private UUID positionId;

    @Column(name = "candidate_id", nullable = false, updatable = false)
    private UUID candidateId;

    @Column(name = "voted_at", nullable = false, updatable = false)
    private LocalDateTime votedAt;

    @PrePersist
    protected void onCreate() {
        this.votedAt = LocalDateTime.now();
    }

    public Vote() {
    }

    public Vote(UUID voteId, UUID voterId, UUID electionId, UUID positionId, UUID candidateId, LocalDateTime votedAt) {
        this.voteId = voteId;
        this.voterId = voterId;
        this.electionId = electionId;
        this.positionId = positionId;
        this.candidateId = candidateId;
        this.votedAt = votedAt;
    }

    public UUID getVoteId() {
        return this.voteId;
    }

    public void setVoteId(UUID voteId) {
        this.voteId = voteId;
    }

    public UUID getVoterId() {
        return this.voterId;
    }

    public void setVoterId(UUID voterId) {
        this.voterId = voterId;
    }

    public UUID getElectionId() {
        return this.electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public UUID getPositionId() {
        return this.positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public UUID getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public LocalDateTime getVotedAt() {
        return this.votedAt;
    }

    public void setVotedAt(LocalDateTime votedAt) {
        this.votedAt = votedAt;
    }
}