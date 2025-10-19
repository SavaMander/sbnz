package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.dto.AddressChangeRequest;
import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.models.Address;
import com.ftn.sbnz.model.models.SuspicionLevel;
import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.model.util.SecurityUtil;
import com.ftn.sbnz.service.repository.AddressRepository;
import com.ftn.sbnz.service.repository.UserRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImplementation implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    private final KieContainer kieContainer;

    public AddressServiceImplementation( KieContainer kieContainer ) {
        this.kieContainer = kieContainer;
    }


    @Override
    public MessageResponse changeAddress(AddressChangeRequest addressChangeRequest) {
        Optional<Address> oldAddressOpt = addressRepository.findAddressByAddress(addressChangeRequest.getOldAddress());

        Optional<User> userOpt = userRepository.findByEmail((addressChangeRequest.getEmail()));
        if(userOpt.isEmpty()) {
            return new MessageResponse(false, "User not found");

        }
        User user = userOpt.get();
        user.setHasChangedAddress(true);

        if(oldAddressOpt.isPresent()) {
            Address oldAddress = oldAddressOpt.get();
            oldAddress.removeAccount();
            addressRepository.save(oldAddress);
        }
        if (addressRepository.findAddressByAddress(addressChangeRequest.getNewAddress()).isEmpty()) {
          Address newAddress = new Address(addressChangeRequest.getNewAddress());
          addressRepository.save(newAddress);
          user.setAddress(newAddress);
        }
        else{
            Optional<Address> addressOpt = addressRepository.findAddressByAddress((addressChangeRequest.getNewAddress()));
            if (addressOpt.isPresent()) {
                Address address = addressOpt.get();
                address.addAccount();
                user.setAddress(address);
                addressRepository.save(address);
            }
        }

        KieSession kieSession = kieContainer.newKieSession("k-session");
		kieSession.addEventListener(new org.kie.api.event.rule.DebugAgendaEventListener());
        try {
			SecurityUtil util = new SecurityUtil();
			kieSession.setGlobal("securityUtil", util);

			kieSession.insert(user);
			if(user.getSuspicionLevel() == SuspicionLevel.SUSPICIOUS) {
                kieSession.getAgenda().getAgendaGroup("process-suspicious-users").setFocus();
                kieSession.fireAllRules();
            } else if (user.getSuspicionLevel() == SuspicionLevel.MALICIOUS) {
                kieSession.getAgenda().getAgendaGroup("process-malicious-users").setFocus();
                kieSession.fireAllRules();
            }
        } finally {
			kieSession.dispose();
		}
        userRepository.save(user);


        return new MessageResponse(true, "Address successfully changed");
    }
}
