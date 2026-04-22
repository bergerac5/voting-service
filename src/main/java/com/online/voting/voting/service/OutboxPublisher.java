package com.online.voting.voting.service;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.voting.voting.models.OutboxEvent;
import com.online.voting.voting.repository.OutboxRepository;

@Service
//
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(OutboxRepository outboxRepository,
            KafkaTemplate<String, Object> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000) // every 5 sec
    public void publishEvents() {

        List<OutboxEvent> events = outboxRepository.findBySentFalse();

        for (OutboxEvent event : events) {
            try {
                Object payload = objectMapper.readValue(event.getPayload(), Object.class);

                kafkaTemplate.send(event.getTopic(), payload);

                event.setSent(true);
                outboxRepository.save(event);

            } catch (Exception e) {
                // log and retry later
                System.err.println("Failed to publish event " + event.getId() + ": " + e.getMessage());
            }
        }
    }
}