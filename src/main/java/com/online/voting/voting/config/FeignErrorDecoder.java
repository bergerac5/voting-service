package com.online.voting.voting.config;

import com.online.voting.voting.handler.CandidateNotFoundException;
import com.online.voting.voting.handler.ElectionNotFoundException;
import com.online.voting.voting.handler.ForbiddenException;
import com.online.voting.voting.handler.PositionNotFoundException;
import com.online.voting.voting.handler.ServiceUnavailableException;
import com.online.voting.voting.handler.UnauthorizedException;
import com.online.voting.voting.handler.VoterNotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    private String resolveServiceName(String url) {
        if (url.contains("/voters"))
            return "voter";
        if (url.contains("/elections"))
            return "election";
        if (url.contains("/positions"))
            return "position";
        if (url.contains("/candidates"))
            return "candidate";
        return "unknown";
    }

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();
        String url = response.request().url();

        System.out.println("❌ Feign Error Occurred!");
        System.out.println("➡️ Method: " + methodKey);
        System.out.println("➡️ URL: " + url);
        System.out.println("➡️ Status Code: " + status);

        if (status == 404) {

            if (url.contains("/voters")) {
                return new VoterNotFoundException("Voter not found");
            }

            if (url.contains("/candidates")) {
                return new CandidateNotFoundException("Candidate not found");
            }

            if (url.contains("/elections")) {
                return new ElectionNotFoundException("Election not found");
            }

            if (url.contains("/positions")) {
                return new PositionNotFoundException("Position not found");
            }
        }

        if (status == 401) {
            return new UnauthorizedException("Unauthorized - Token missing or invalid");
        }

        if (status == 403) {
            return new ForbiddenException("Forbidden - Access denied");
        }

        if (status == 500) {
            String serviceName = resolveServiceName(url); // "voter", "election", "position", "candidate"
            return new ServiceUnavailableException(serviceName, "Downstream service unavailable: " + url);
        }

        return new RuntimeException("Unexpected error //error decoder: HTTP " + status + " from " + url);
    }
}