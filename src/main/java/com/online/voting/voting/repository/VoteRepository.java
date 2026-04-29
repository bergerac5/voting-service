package com.online.voting.voting.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.online.voting.voting.models.Vote;

import jakarta.persistence.LockModeType;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

        // 🔹 Check if voter already voted for a specific candidate
        boolean existsByVoterIdAndElectionIdAndPositionId(
                        UUID voterId,
                        UUID electionId,
                        UUID positionId);

        // 🔹 Count how many votes voter has cast for a position (for maxVotes
        // validation)
        long countByVoterIdAndPositionId(
                        UUID voterId,
                        UUID positionId);

        // 🔹 Fetch all votes for a position (temporary until Result Service fully
        // handles reads)
        List<Vote> findByPositionId(UUID positionId);

        // 🔹 Count votes per candidate for a position (basic aggregation)
        @Query("""
                        SELECT v.candidateId, COUNT(v)
                        FROM Vote v
                        WHERE v.positionId = :positionId
                        GROUP BY v.candidateId
                        """)
        List<Object[]> countVotesByPosition(@Param("positionId") UUID positionId);

        // 🔹 Optional: Lock voter votes (extra concurrency safety if needed)
        @Lock(LockModeType.PESSIMISTIC_READ)
        @Query("""
                        SELECT v
                        FROM Vote v
                        WHERE v.voterId = :voterId
                        AND v.positionId = :positionId
                        """)
        List<Vote> findVotesForUpdate(
                        @Param("voterId") UUID voterId,
                        @Param("positionId") UUID positionId);

}
