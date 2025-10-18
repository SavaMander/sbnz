package com.ftn.sbnz.model.dto;

public class LoginResponse {

    private String jwt;

    public LoginResponse() {
    }

    public String getJwt() {
        return jwt;
    }

    public LoginResponse(String jwt) {
        this.jwt = jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
