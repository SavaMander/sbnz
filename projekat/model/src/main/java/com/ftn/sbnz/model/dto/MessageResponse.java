package com.ftn.sbnz.model.dto;

public class MessageResponse {
    private Boolean successful;
    private String message;

    public MessageResponse() {
    }

    public MessageResponse(Boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
