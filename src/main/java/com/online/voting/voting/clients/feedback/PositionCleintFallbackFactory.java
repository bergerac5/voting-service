package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.PositionClient;
import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.PositionResponse;
import com.online.voting.voting.handler.ForbiddenException;
import com.online.voting.voting.handler.PositionNotFoundException;
import com.online.voting.voting.handler.ServiceUnavailableException;
import com.online.voting.voting.handler.UnauthorizedException;

@Component
public class PositionCleintFallbackFactory implements FallbackFactory<PositionClient> {
    private static final Logger log = LoggerFactory.getLogger(CandidateClientFallbackFactory.class);

    @Override
    public PositionClient create(Throwable cause) {
        return new PositionClient() {
            @Override
            public ApiResponse<PositionResponse> getPosition(UUID positionId) {
                throw translate(cause);
            }

            @Override
            public ApiResponse<List<PositionResponse>> getPositionsByIds(UUID[] ids) {
                throw translate(cause);
            }
        };
    }

    private RuntimeException translate(Throwable cause) {
        if (cause instanceof PositionNotFoundException
                || cause instanceof UnauthorizedException
                || cause instanceof ForbiddenException) {
            log.debug("Passing through decoded exception: {}", cause.getClass().getSimpleName());
            if (cause instanceof RuntimeException re) {
                return re;
            }
        }
        // Everything else (connection refused, timeout, 500, 503, decode failure) =
        // real unavailability
        log.warn("Position service unavailable, triggering fallback", cause);
        return new ServiceUnavailableException(ServiceNames.POSITION, "Position service is currently unavailable");
    }
}
