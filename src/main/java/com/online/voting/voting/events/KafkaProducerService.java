package com.online.voting.voting.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.online.voting.events.voting.VoteEvent;
import com.online.voting.voting.models.Vote;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishVoteEvent(Vote vote) {

        VoteEvent voteEvent = new VoteEvent(
                vote.getVoteId(),
                vote.getElectionId(),
                vote.getPositionId(),
                vote.getCandidateId(),
                vote.getVoterId());

        kafkaTemplate.send("vote-casted", voteEvent);
    }

}
