package com.ftn.sbnz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    public String username;
    public String email;
    public String address;
    public String phoneNumber;
    public String suspicionLevel;
}
