package com.abaiyat.triagews.service.impl;

import com.abaiyat.triagews.model.AddressEntity;
import com.abaiyat.triagews.model.UserEntity;
import com.abaiyat.triagews.repository.UserRepository;
import com.abaiyat.triagews.service.AddressService;
import com.abaiyat.triagews.shared.dto.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;

    @Autowired
    public AddressServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AddressDTO> getAddresses(String userId) {
        List<AddressDTO> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) return returnValue;

        Iterable<AddressEntity> addresses = userEntity.getAddresses();

        for (AddressEntity addressEntity: addresses) {
            returnValue.add(modelMapper.map(addressEntity, AddressDTO.class));
        }

        return returnValue;
    }
}
