package com.ftn.sbnz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressChangeRequest {
    String email;
    String oldAddress;
    String newAddress;
}
