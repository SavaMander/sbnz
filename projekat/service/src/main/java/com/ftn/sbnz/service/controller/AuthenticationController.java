package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.dto.*;
import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.service.repository.UserRepository;
import com.ftn.sbnz.service.security.jwt.JwtTokenUtil;
import com.ftn.sbnz.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


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
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        Optional<User> opt = userRepository.findByEmail(loginRequest.getUsername());
            if(opt.isPresent()){
                User usr = opt.get();
                if(usr.getBlockExpirationDate()!=null) {
                    if (usr.getBlockExpirationDate().isBefore(Instant.now())) {
                        usr.setBlockExpirationDate(null);
                        usr.setSuspended(false);
                        userRepository.save(usr);
                    }
                }
            }
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        User user= (User) auth.getPrincipal();
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

    @GetMapping(value = "/profile")
    public ResponseEntity<Profile> profile(@RequestParam String email){
        Profile profile = userService.getProfile(email);
        return ResponseEntity.ok(profile);
    }

    @PostMapping(value = "/add-code")
    public ResponseEntity<MessageResponse> addCode(@RequestParam String email, @RequestParam String code){
        MessageResponse messageResponse = userService.addCode(email, code);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<Profile>> getUsers(){
        List<Profile> profiles = userService.getUsers();
        return ResponseEntity.ok(profiles);
    }
}
