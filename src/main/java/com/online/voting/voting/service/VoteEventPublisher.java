package com.online.voting.voting.service;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.online.voting.events.voting.VoteEvent;
import com.online.voting.voting.models.Vote;

@Service
public class VoteEventPublisher {

    private final StreamBridge streamBridge;

    public VoteEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishVoteEvent(Vote vote) {

        VoteEvent voteEvent = new VoteEvent(
                vote.getVoteId(),
                vote.getElectionId(),
                vote.getPositionId(),
                vote.getCandidateId(),
                vote.getVoterId(),
                vote.getVotedAt());

        streamBridge.send(
                "voteCasted-out-0",
                voteEvent);
    }
}