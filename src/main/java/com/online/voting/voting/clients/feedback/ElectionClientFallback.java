package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.ElectionClient;
import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.ElectionResponse;
import com.online.voting.voting.handler.ServiceUnavailableException;

@Component
public class ElectionClientFallback implements ElectionClient {

    @Override
    public ApiResponse<ElectionResponse> getElectionById(UUID id) {
        throw new ServiceUnavailableException(ServiceNames.POSITION, "Election service is currently unavailable");
    }

    @Override
    public ApiResponse<List<ElectionResponse>> getElectionsByIds(List<UUID> ids) {
        throw new ServiceUnavailableException(ServiceNames.ELECTION, "Election service is currently unavailable");
    }

}
