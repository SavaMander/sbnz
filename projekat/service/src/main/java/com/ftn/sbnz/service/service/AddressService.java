package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.dto.AddressChangeRequest;
import com.ftn.sbnz.model.dto.MessageResponse;

public interface AddressService {

    public MessageResponse changeAddress(AddressChangeRequest addressChangeRequest);
}
