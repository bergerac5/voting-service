package com.online.voting.voting.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.voting.voting.dtos.ApiResponse;
import com.online.voting.voting.dtos.CastVoteRequest;
import com.online.voting.voting.dtos.CastVoteResponse;
import com.online.voting.voting.service.VoteService;

@RestController
@RequestMapping("/votes")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ApiResponse<CastVoteResponse> vote(@RequestBody CastVoteRequest request) {

        CastVoteResponse response = voteService.castVote(request);

        return ApiResponse.success("Vote cast successfully", response);
    }
}
