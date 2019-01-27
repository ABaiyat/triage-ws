package com.abaiyat.triagews.service;

import com.abaiyat.triagews.UserDto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
}
