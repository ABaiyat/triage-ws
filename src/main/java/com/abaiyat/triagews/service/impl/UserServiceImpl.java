package com.abaiyat.triagews.service.impl;

import com.abaiyat.triagews.model.UserModel;
import com.abaiyat.triagews.repository.UserRepository;
import com.abaiyat.triagews.service.UserService;
import com.abaiyat.triagews.shared.dto.UserDto;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        userModel.setUserId("A");

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
