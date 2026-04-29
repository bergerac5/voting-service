package com.online.voting.voting.clients.feedback;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.PositionClient;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.PositionResponse;

@Component
public class PositionCleintFeedBack implements PositionClient {
    @Override
    public ApiResponse<PositionResponse> getPosition(UUID positionId) {
        return new ApiResponse<>(false, "Position service is currently unavailable", null);
    }

    @Override
    public ApiResponse<List<PositionResponse>> getPositionsByIds(UUID[] ids) {
        return new ApiResponse<>(false, "Position service is currently unavailable", Collections.emptyList());
    }

}
