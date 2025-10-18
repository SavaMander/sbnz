package com.ftn.sbnz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String passwordRepeat;
    private String phoneNumber;
    private String address;
    private String creditCardNumber;
}
