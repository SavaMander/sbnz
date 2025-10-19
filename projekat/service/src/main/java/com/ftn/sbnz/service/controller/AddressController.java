package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.dto.AddressChangeRequest;
import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.service.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }
    @PutMapping("/change")
    public ResponseEntity<MessageResponse> changeUserAddress(@RequestBody AddressChangeRequest addressChangeRequest, String email) {
        addressChangeRequest.setEmail(email);

        MessageResponse response = addressService.changeAddress(addressChangeRequest);

        if (response.getSuccessful()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
