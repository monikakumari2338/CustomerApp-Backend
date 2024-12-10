package com.deepanshu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepanshu.modal.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
