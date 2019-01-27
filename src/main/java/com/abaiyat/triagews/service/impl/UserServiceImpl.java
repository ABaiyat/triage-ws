package com.abaiyat.triagews.service.impl;

import com.abaiyat.triagews.model.UserModel;
import com.abaiyat.triagews.repository.UserRepository;
import com.abaiyat.triagews.service.UserService;
import com.abaiyat.triagews.shared.dto.UserDto;
import com.abaiyat.triagews.shared.dto.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Utils utils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Utils utils) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.utils = utils;
    }

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null ||
                userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Record Already Exists");
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        userModel.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userModel.setUserId(utils.generateUserId(30));

        UserModel storedUserDetails = userRepository.save(userModel);

        UserDto returnDto = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnDto);
        return returnDto;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
