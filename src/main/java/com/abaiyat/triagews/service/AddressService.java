package com.abaiyat.triagews.service;

import com.abaiyat.triagews.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddresses(String userId);
}
