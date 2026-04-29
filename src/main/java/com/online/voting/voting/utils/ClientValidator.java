package com.online.voting.voting.utils;

import com.online.voting.voting.dtos.ApiResponse;

public class ClientValidator {

    public static <T> T validate(ApiResponse<T> response, RuntimeException exception) {

        if (response == null) {
            throw new RuntimeException("Service returned null response");
        }

        if (!response.isSuccess()) {
            throw exception;
        }

        if (response.getData() == null) {
            throw exception;
        }

        return response.getData();
    }
}