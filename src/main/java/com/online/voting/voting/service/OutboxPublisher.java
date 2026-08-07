package com.online.voting.voting.service;

import java.util.List;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.voting.events.voting.VoteEvent;
import com.online.voting.voting.models.OutboxEvent;
import com.online.voting.voting.repository.OutboxRepository;

@Service
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(
            OutboxRepository outboxRepository,
            StreamBridge streamBridge,
            ObjectMapper objectMapper) {

        this.outboxRepository = outboxRepository;
        this.streamBridge = streamBridge;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishEvents() {
        System.out.println("Publisher running...");

        List<OutboxEvent> events = outboxRepository.findBySentFalse();

        for (OutboxEvent event : events) {

            VoteEvent payload = deserialize(event);

            streamBridge.send(
                    "voteCasted-out-0",
                    payload);

            event.setSent(true);

            outboxRepository.save(event);
        }
    }

    private VoteEvent deserialize(
            OutboxEvent outboxEvent) {

        try {

            return objectMapper.readValue(
                    outboxEvent.getPayload(),
                    VoteEvent.class);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}