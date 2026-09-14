package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.ElectionClient;
import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.ElectionResponse;
import com.online.voting.voting.handler.ElectionNotFoundException;
import com.online.voting.voting.handler.ForbiddenException;
import com.online.voting.voting.handler.ServiceUnavailableException;
import com.online.voting.voting.handler.UnauthorizedException;

@Component
public class ElectionClientFallbackFactory implements FallbackFactory<ElectionClient> {

    private static final Logger log = LoggerFactory.getLogger(ElectionClientFallbackFactory.class);

    @Override
    public ElectionClient create(Throwable cause) {
        return new ElectionClient() {
            @Override
            public ApiResponse<ElectionResponse> getElectionById(UUID id) {
                throw translate(cause);
            }

            @Override
            public ApiResponse<List<ElectionResponse>> getElectionsByIds(List<UUID> ids) {
                throw translate(cause);
            }
        };
    }

    private RuntimeException translate(Throwable cause) {

        if (cause instanceof ElectionNotFoundException
                || cause instanceof UnauthorizedException
                || cause instanceof ForbiddenException) {
            log.debug("Passing through decoded exception: {}", cause.getClass().getSimpleName());
            if (cause instanceof RuntimeException re) {
                return re;
            }
        }
        // Everything else (connection refused, timeout, 500, 503, decode failure) =
        // real unavailability
        log.warn("Election service unavailable, triggering fallback", cause);
        return new ServiceUnavailableException(ServiceNames.ELECTION, "Election service is currently unavailable");
    }
}
