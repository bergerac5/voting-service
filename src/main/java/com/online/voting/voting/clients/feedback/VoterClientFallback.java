package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.clients.VoterClient;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.VoterResponse;
import com.online.voting.voting.handler.ServiceUnavailableException;

@Component
public class VoterClientFallback implements VoterClient {

    @Override
    public ApiResponse<VoterResponse> getVoterById(UUID voterId) {
        throw new ServiceUnavailableException(ServiceNames.VOTER, "Voter service is currently unavailable");
    }

    @Override
    public ApiResponse<List<VoterResponse>> getVotersByIds(List<UUID> ids) {
        throw new ServiceUnavailableException(ServiceNames.VOTER, "Voter service is currently unavailable");
    }
}