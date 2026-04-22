package com.online.voting.voting.config;

import com.online.voting.voting.handler.CandidateNotFoundException;
import com.online.voting.voting.handler.ElectionNotFoundException;
import com.online.voting.voting.handler.PositionNotFoundException;
import com.online.voting.voting.handler.VoterNotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

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
            return new RuntimeException("Unauthorized - Token missing or invalid");
        }

        if (status == 403) {
            return new RuntimeException("Forbidden - Access denied");
        }

        if (status == 500) {
            return new RuntimeException("Internal server error from " + url);
        }

        return new RuntimeException("Unexpected error //error decoder: HTTP " + status + " from " + url);
    }
}