package com.online.voting.voting.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.voting.events.voting.VoteEvent;
import com.online.voting.voting.clients.CandidateClient;
import com.online.voting.voting.clients.ElectionClient;
import com.online.voting.voting.clients.PositionClient;
import com.online.voting.voting.clients.VoterClient;
import com.online.voting.voting.dtos.CandidateResponse;
import com.online.voting.voting.dtos.CastVoteRequest;
import com.online.voting.voting.dtos.CastVoteResponse;
import com.online.voting.voting.dtos.ElectionResponse;
import com.online.voting.voting.dtos.PositionResponse;
import com.online.voting.voting.dtos.VoterResponse;
import com.online.voting.voting.events.KafkaProducerService;
import com.online.voting.voting.handler.CandidateNotFoundException;
import com.online.voting.voting.handler.DuplicateVoteException;
import com.online.voting.voting.handler.ElectionNotFoundException;
import com.online.voting.voting.handler.PositionNotFoundException;
import com.online.voting.voting.handler.VoterNotFoundException;
import com.online.voting.voting.handler.VotingNotAllowedException;
import com.online.voting.voting.models.OutboxEvent;
import com.online.voting.voting.models.Vote;
import com.online.voting.voting.repository.OutboxRepository;
import com.online.voting.voting.repository.VoteRepository;
import static com.online.voting.voting.utils.ClientValidator.validate;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final KafkaProducerService kafkaProducer;
    private final ElectionClient electionClient;
    private final PositionClient positionClient;
    private final CandidateClient candidateClient;
    private final VoterClient voterClient;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public VoteService(VoteRepository voteRepository,
            KafkaProducerService kafkaProducer,
            ElectionClient electionClient,
            PositionClient positionClient,
            CandidateClient candidateClient,
            VoterClient voterClient,
            OutboxRepository outboxRepository,
            ObjectMapper objectMapper) {
        this.voteRepository = voteRepository;
        this.kafkaProducer = kafkaProducer;
        this.electionClient = electionClient;
        this.positionClient = positionClient;
        this.candidateClient = candidateClient;
        this.voterClient = voterClient;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CastVoteResponse castVote(CastVoteRequest request) {

        // 1. Election FIRST (fail fast)
        ElectionResponse election = validate(
                electionClient.getElectionById(request.getElectionId()),
                new ElectionNotFoundException(
                        String.format("Election not found with ID: %s", request.getElectionId())));

        // 🔥 Enforce rule (Option 3)
        if (!election.isCanVote()) {
            throw new VotingNotAllowedException("Election is not open for voting");
        }

        // 2. Validate other entities
        VoterResponse voter = validate(
                voterClient.getVoterById(request.getVoterId()),
                new VoterNotFoundException(
                        String.format("Voter not found with ID: %s", request.getVoterId())));

        PositionResponse position = validate(
                positionClient.getPosition(request.getPositionId()),
                new PositionNotFoundException(
                        String.format("Position not found with ID: %s", request.getPositionId())));

        CandidateResponse candidate = validate(
                candidateClient.getCandidate(request.getCandidateId()),
                new CandidateNotFoundException(
                        String.format("Candidate not found with ID: %s", request.getCandidateId())));

        // 2. Create vote
        Vote vote = new Vote();
        vote.setVoterId(request.getVoterId());
        vote.setCandidateId(request.getCandidateId());
        vote.setElectionId(request.getElectionId());
        vote.setPositionId(request.getPositionId());
        vote.setVotedAt(LocalDateTime.now());

        // 3. Save vote safely (DB constraint handles duplicates)
        try {
            voteRepository.save(vote);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateVoteException("Voter has already voted for this position");
        }

        // 4. Create event
        VoteEvent voteEvent = new VoteEvent(
                vote.getVoteId(),
                vote.getElectionId(),
                vote.getPositionId(),
                vote.getCandidateId(),
                vote.getVoterId());

        // 5. Save to Outbox
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setTopic("vote-casted");
        outboxEvent.setPayload(convertToJson(voteEvent));
        outboxEvent.setSent(false);
        outboxEvent.setCreatedAt(LocalDateTime.now());

        outboxRepository.save(outboxEvent);

        // 6. Return response
        return new CastVoteResponse(
                vote.getVoteId(),
                election.getTitle(),
                position.getName(),
                buildCandidateName(candidate),
                "Voted successfully");
    }

    // ✅ Helper: JSON conversion
    private String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    // ✅ Helper: Candidate name builder
    private String buildCandidateName(CandidateResponse candidate) {
        String first = candidate.getFirstName() != null ? candidate.getFirstName() + " " : "";
        String last = candidate.getLastName() != null ? candidate.getLastName() : candidate.getUsername();
        return first + last;
    }
}