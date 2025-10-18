package com.ftn.sbnz.service.repository;

import com.ftn.sbnz.model.models.Address;
import com.ftn.sbnz.model.models.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPAddressRepository extends JpaRepository<IPAddress, UUID> {
    public Optional<IPAddress> findIPAddressByAddress(String ipAddress);
}
