package com.online.voting.voting.clients.feedback;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.CandidateClient;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.CandidateResponse;

@Component
public class CandidateClientFeedBack implements CandidateClient {

    @Override
    public ApiResponse<CandidateResponse> getCandidate(UUID candidateId) {
        return new ApiResponse<>(false, "Candidate service is currently unavailable", null);
    }

    @Override
    public ApiResponse<List<CandidateResponse>> getCandidatesByIds(List<UUID> ids) {
        return new ApiResponse<>(false, "Candidate service is currently unavailable", Collections.emptyList());
    }

}
