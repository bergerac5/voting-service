package com.online.voting.voting.clients.feedback;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.online.voting.voting.clients.CandidateClient;
import com.online.voting.voting.clients.ServiceNames;
import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.CandidateResponse;
import com.online.voting.voting.handler.CandidateNotFoundException;
import com.online.voting.voting.handler.ForbiddenException;
import com.online.voting.voting.handler.ServiceUnavailableException;
import com.online.voting.voting.handler.UnauthorizedException;

@Component
public class CandidateClientFallbackFactory implements FallbackFactory<CandidateClient> {

    private static final Logger log = LoggerFactory.getLogger(CandidateClientFallbackFactory.class);

    @Override
    public CandidateClient create(Throwable cause) {
        return new CandidateClient() {
            @Override
            public ApiResponse<CandidateResponse> getCandidate(UUID candidateId) {
                throw translate(cause);
            }

            @Override
            public ApiResponse<List<CandidateResponse>> getCandidatesByIds(List<UUID> ids) {
                throw translate(cause);
            }
        };

    }

    private RuntimeException translate(Throwable cause) {

        if (cause instanceof CandidateNotFoundException
                || cause instanceof UnauthorizedException
                || cause instanceof ForbiddenException) {
            log.debug("Passing through decoded exception: {}", cause.getClass().getSimpleName());
            {
                if (cause instanceof RuntimeException re)
                    return re;
            }
        }
        // Everything else (connection refused, timeout, 500, 503, decode failure) =
        // real unavailability
        log.warn("Candidate service unavailable, triggering fallback", cause);
        return new ServiceUnavailableException(ServiceNames.CANDIDATE, "Candidate service is currently unavailable");
    }

}
