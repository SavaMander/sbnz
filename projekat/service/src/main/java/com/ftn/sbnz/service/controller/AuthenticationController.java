package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.dto.LoginRequest;
import com.ftn.sbnz.model.dto.LoginResponse;
import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.dto.RegistrationRequest;
import com.ftn.sbnz.service.security.jwt.JwtTokenUtil;
import com.ftn.sbnz.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        User user= (User) auth.getPrincipal();
        boolean hasSuperAdminRole = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_Admin"));
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        String token = jwtTokenUtil.generateToken((UserDetails) auth.getPrincipal());
        LoginResponse loginResponse=new LoginResponse(token);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegistrationRequest registrationRequest){
        MessageResponse messageResponse = userService.registerUser(registrationRequest);
        return ResponseEntity.ok(messageResponse);
    }
}
