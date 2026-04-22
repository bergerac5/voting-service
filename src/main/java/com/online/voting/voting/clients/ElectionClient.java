package com.online.voting.voting.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.ElectionResponse;

@FeignClient(name = "election-service", url = "http://localhost:8084"/*
                                                                      * , configuration = FeignConfig.class, fallback =
                                                                      * ElectionClientFeedBack.class
                                                                      */)
public interface ElectionClient {

    @GetMapping("/elections/{electionId}")
    ApiResponse<ElectionResponse> getElection(@PathVariable UUID electionId);

    // bulk endpoint to get multiple elections by their IDs
    @PostMapping("/elections/bulk")
    ApiResponse<List<ElectionResponse>> getElectionsByIds(@RequestBody List<UUID> ids);

}
