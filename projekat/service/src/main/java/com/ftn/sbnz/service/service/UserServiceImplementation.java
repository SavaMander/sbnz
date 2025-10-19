package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.dto.Profile;
import com.ftn.sbnz.model.dto.RegistrationRequest;
import com.ftn.sbnz.model.models.Address;
import com.ftn.sbnz.model.models.CreditCard;
import com.ftn.sbnz.model.models.Role;
import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.model.util.SecurityUtil;
import com.ftn.sbnz.service.repository.AddressRepository;
import com.ftn.sbnz.service.repository.CreditCardRepository;
import com.ftn.sbnz.service.repository.UserRepository;
import org.aspectj.bridge.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    private final KieContainer kieContainer;

    UserServiceImplementation( KieContainer kieContainer ) {
        this.kieContainer = kieContainer;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        return userOptional.orElse(null);
    }

    @Transactional
    public MessageResponse registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return new MessageResponse(false, "User already exists");
        }
        if(!registrationRequest.getPassword().equals(registrationRequest.getPasswordRepeat())){
            return new MessageResponse(false, "Passwords don't match");
        }

        Address address;
        Optional<Address> opt1 = addressRepository.findAddressByAddress(registrationRequest.getAddress());
        if (opt1.isPresent()) {
            address = opt1.get();
            address.addAccount();
        } else {
            address = new Address(registrationRequest.getAddress());
        }
        addressRepository.save(address);

        CreditCard creditCard;
        Optional<CreditCard> opt2 = creditCardRepository.findCreditCardByNumber(registrationRequest.getCreditCardNumber());
        if (opt2.isPresent()) {
            creditCard = opt2.get();
            creditCard.addAccount();
        } else {
            creditCard = new CreditCard(registrationRequest.getCreditCardNumber());
        }
        creditCardRepository.save(creditCard);
        User user = new User(
                    registrationRequest.getUsername(),
                    registrationRequest.getEmail(),
                    passwordEncoder.encode(registrationRequest.getPassword()),
                    address,
                    creditCard,
                    registrationRequest.getPhoneNumber(),
                    Role.User
        );

        KieSession kieSession = kieContainer.newKieSession("k-session");
        kieSession.addEventListener(new org.kie.api.event.rule.DebugAgendaEventListener());
        try {
            SecurityUtil util = new SecurityUtil();
            kieSession.setGlobal("securityUtil", util);
            kieSession.insert(creditCard);
            kieSession.insert(address);
            kieSession.insert(user);

            System.out.println("Firing all rules...");
            int rulesFired = kieSession.fireAllRules();
            System.out.println("Finished. Rules fired: " + rulesFired);
        } finally {
            kieSession.dispose();
        }
        userRepository.save(user);
        return new MessageResponse(true, "Successfully registered");
    }

    public Profile getProfile(String email){
        Optional<User> opt = userRepository.findByEmail(email);
        if(opt.isEmpty()){
            return null;
        }
        User user = opt.get();
        Profile profile = new Profile(user.getRealUsername(),user.getEmail(),user.getAddress().getAddress(),user.getPhone(),user.getSuspicionLevel().toString());
        return profile;
    }

    public MessageResponse addCode(String email, String code){
        Optional<User> opt = userRepository.findByEmail(email);
        if(opt.isEmpty()){
            return new MessageResponse(false, "User doesnt exist");
        }
        User user = opt.get();
        user.addPromoCode(code);
        KieSession kieSession = kieContainer.newKieSession("k-session");
        kieSession.addEventListener(new org.kie.api.event.rule.DebugAgendaEventListener());
        try {
            SecurityUtil util = new SecurityUtil();
            kieSession.setGlobal("securityUtil", util);
            kieSession.insert(user);
            System.out.println("Firing all rules...");
            int rulesFired = kieSession.fireAllRules();
            System.out.println("Finished. Rules fired: " + rulesFired);
        } finally {
            kieSession.dispose();
        }
        userRepository.save(user);

        return new MessageResponse(true, "Promo code is successfully added");
    }

    public List<Profile> getUsers(){
        List<User> users = userRepository.findAll();
        List<Profile> profiles =  new ArrayList<Profile>();
        for (User user: users) {
            Profile p = new Profile(user.getRealUsername(),user.getEmail(),user.getAddress().getAddress(),user.getPhone(),user.getSuspicionLevel().toString());
            profiles.add(p);
        }
        return profiles;
    }
}
