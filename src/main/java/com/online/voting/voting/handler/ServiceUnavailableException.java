package com.online.voting.voting.handler;

public class ServiceUnavailableException extends RuntimeException {
    private final String serviceName;

    public ServiceUnavailableException(String serviceName, String message) {
        super(message);
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
