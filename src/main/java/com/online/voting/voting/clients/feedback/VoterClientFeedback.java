package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.VoterClient;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.VoterResponse;

@Component
public class VoterClientFeedback implements VoterClient {

    @Override
    public ApiResponse<VoterResponse> getVoterById(UUID voterId) {
        throw new RuntimeException("Voter service unavailable!!!!!!!!!!");
    }

    @Override
    public ApiResponse<List<VoterResponse>> getVotersByIds(List<UUID> ids) {
        return ApiResponse.error("Voter service unavailable");
    }

}
