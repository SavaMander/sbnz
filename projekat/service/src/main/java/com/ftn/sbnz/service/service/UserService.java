package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.dto.RegistrationRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public MessageResponse registerUser(RegistrationRequest registrationRequest);
}
