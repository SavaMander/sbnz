package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.dto.Profile;
import com.ftn.sbnz.model.dto.RegistrationRequest;
import com.ftn.sbnz.model.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    public MessageResponse registerUser(RegistrationRequest registrationRequest);
    public Profile getProfile(String email);
    public MessageResponse addCode(String email, String code);
    public List<Profile> getUsers();
}
