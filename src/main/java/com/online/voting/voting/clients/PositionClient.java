package com.online.voting.voting.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.PositionResponse;

@FeignClient(name = "api-gateway", url = "http://localhost:8080"/*
                                                                 * , configuration = FeignConfig.class, fallback =
                                                                 * PositionCleintFeedBack.class
                                                                 */)
public interface PositionClient {

    @GetMapping("/positions/{positionId}")
    ApiResponse<PositionResponse> getPosition(@PathVariable UUID positionId);

    // bulk endpoint to get multiple positions by their IDs
    @GetMapping("/positions/bulk")
    ApiResponse<List<PositionResponse>> getPositionsByIds(@PathVariable UUID[] ids);

}
