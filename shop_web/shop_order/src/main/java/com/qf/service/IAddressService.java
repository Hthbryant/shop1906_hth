package com.qf.service;

import com.qf.entity.Address;

import java.util.List;

public interface IAddressService {
    List<Address> getUserAddress(Integer uid);

    int addAddress(Address address);
}
