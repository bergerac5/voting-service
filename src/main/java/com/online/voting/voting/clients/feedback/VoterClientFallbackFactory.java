package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.clients.VoterClient;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.VoterResponse;
import com.online.voting.voting.handler.ForbiddenException;
import com.online.voting.voting.handler.ServiceUnavailableException;
import com.online.voting.voting.handler.UnauthorizedException;
import com.online.voting.voting.handler.VoterNotFoundException;

@Component
public class VoterClientFallbackFactory implements FallbackFactory<VoterClient> {

    private static final Logger log = LoggerFactory.getLogger(VoterClientFallbackFactory.class);

    @Override
    public VoterClient create(Throwable cause) {
        return new VoterClient() {
            @Override
            public ApiResponse<VoterResponse> getVoterById(UUID voterId) {
                throw translate(cause);
            }

            @Override
            public ApiResponse<List<VoterResponse>> getVotersByIds(List<UUID> ids) {
                throw translate(cause);
            }
        };
    }

    private RuntimeException translate(Throwable cause) {
        // Let deliberate, decoder-thrown business exceptions pass through unchanged
        if (cause instanceof VoterNotFoundException
                || cause instanceof UnauthorizedException
                || cause instanceof ForbiddenException) {
            log.debug("Passing through decoded exception: {}", cause.getClass().getSimpleName());
            if (cause instanceof RuntimeException re)
                return re;
        }

        // Everything else (connection refused, timeout, 500, 503, decode failure) =
        // real unavailability
        log.warn("Voter service unavailable, triggering fallback", cause);
        return new ServiceUnavailableException(ServiceNames.VOTER, "Voter service is currently unavailable");
    }
}