package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.CandidateClient;
import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.CandidateResponse;
import com.online.voting.voting.handler.ServiceUnavailableException;

@Component
public class CandidateClientFallback implements CandidateClient {

    @Override
    public ApiResponse<CandidateResponse> getCandidate(UUID candidateId) {
        throw new ServiceUnavailableException(ServiceNames.CANDIDATE, "Candidate service is currently unavailable");
    }

    @Override
    public ApiResponse<List<CandidateResponse>> getCandidatesByIds(List<UUID> ids) {
        throw new ServiceUnavailableException(ServiceNames.CANDIDATE, "    Candidate service is currently unavailable");
    }

}
