package com.online.voting.voting.clients.feedback;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.ElectionClient;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.ElectionResponse;

@Component
public class ElectionClientFeedBack implements ElectionClient {

    @Override
    public ApiResponse<ElectionResponse> getElectionById(UUID id) {
        throw new com.online.voting.voting.handler.ServiceUnavailableException("Election service unavailable");
    }

    @Override
    public ApiResponse<List<ElectionResponse>> getElectionsByIds(List<UUID> ids) {
        return new ApiResponse<>(false, "Election service is currently unavailable", Collections.emptyList());
    }

}
