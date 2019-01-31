package com.abaiyat.triagews.service.impl;

import com.abaiyat.triagews.model.UserEntity;
import com.abaiyat.triagews.repository.UserRepository;
import com.abaiyat.triagews.shared.dto.AddressDTO;
import com.abaiyat.triagews.shared.dto.UserDto;
import com.abaiyat.triagews.shared.dto.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "12314sdf4ffds";
    String encryptedPassword = "190312mf9sdnf";

    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId("123");
        userEntity.setEmail("test@test.com");
        userEntity.setName("Amaan");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
    }

    @Test
    void testGetUser() {


        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Amaan", userDto.getName());
    }

    @Test
    final void testGetUser_UsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser("test@test.com");
        });
    }

    @Test
    final void testCreateUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("hg092123123fd");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        AddressDTO addressDto = new AddressDTO();
        addressDto.setType("shipping");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDto);

        UserDto userDto = new UserDto();
        userDto.setAddresses(addresses);

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getName(), storedUserDetails.getName());
    }
}