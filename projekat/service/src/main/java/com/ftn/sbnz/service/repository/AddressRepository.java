package com.ftn.sbnz.service.repository;

import com.ftn.sbnz.model.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    public Optional<Address> findAddressByAddress(String address);
}
