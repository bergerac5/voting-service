package com.online.voting.voting.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.CandidateResponse;

@FeignClient(name = "candidate-service", url = "http://localhost:8083"/*
                                                                       * , configuration = FeignConfig.class, fallback =
                                                                       * CandidateClientFeedBack.class
                                                                       */)
public interface CandidateClient {
    @GetMapping("/candidates/{candidateId}")
    ApiResponse<CandidateResponse> getCandidate(@PathVariable UUID candidateId);

    @PostMapping("/candidates/bulk")
    ApiResponse<List<CandidateResponse>> getCandidatesByIds(@RequestBody List<UUID> ids);

    //
}
