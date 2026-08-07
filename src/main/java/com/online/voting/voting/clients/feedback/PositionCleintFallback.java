package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.PositionClient;
import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.PositionResponse;
import com.online.voting.voting.handler.ServiceUnavailableException;

@Component
public class PositionCleintFallback implements PositionClient {
    @Override
    public ApiResponse<PositionResponse> getPosition(UUID positionId) {
        throw new ServiceUnavailableException(ServiceNames.POSITION, "Position service is currently unavailable");
    }

    @Override
    public ApiResponse<List<PositionResponse>> getPositionsByIds(UUID[] ids) {
        throw new ServiceUnavailableException(ServiceNames.POSITION, "Position service is currently unavailable");
    }

}
