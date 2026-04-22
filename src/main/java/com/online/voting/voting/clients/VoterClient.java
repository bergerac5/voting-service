package com.online.voting.voting.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.VoterResponse;

@FeignClient(name = "api-gateway", url = "http://localhost:8080"/* , configuration = FeignConfig.class */)
public interface VoterClient {

    // Define methods to interact with voter service here: getVoterById,
    // getVotersByIds

    @GetMapping("/voters/{voterId}")
    ApiResponse<VoterResponse> getVoterById(@PathVariable UUID voterId);

    @PostMapping("/voters/bulk")
    ApiResponse<List<VoterResponse>> getVotersByIds(@RequestBody List<UUID> ids);

}
