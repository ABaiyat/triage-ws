package com.abaiyat.triagews.service.impl;

import com.abaiyat.triagews.exceptions.UserServiceException;
import com.abaiyat.triagews.model.UserModel;
import com.abaiyat.triagews.repository.UserRepository;
import com.abaiyat.triagews.service.UserService;
import com.abaiyat.triagews.shared.dto.UserDto;
import com.abaiyat.triagews.shared.dto.Utils;
import com.abaiyat.triagews.ui.model.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public UserDto getUser(String email) {
        UserModel userModel = userRepository.findByEmail(email);

        if (userModel == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userModel, returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByEmail(email);

        if (userModel == null) throw new UsernameNotFoundException(email);

        return new User(userModel.getEmail(), userModel.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserModel user = userRepository.findByUserId(userId);

        if (user == null) throw new UsernameNotFoundException("User with ID: " + userId + " not found");

        BeanUtils.copyProperties(user, returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        UserModel userModel = userRepository.findByUserId(userId);

        if (userModel == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userModel.setName(user.getName());
        userModel.setUsername(user.getUsername());

        UserModel updatedUserDetails = userRepository.save(userModel);
        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserModel userModel = userRepository.findByUserId(userId);
        if (userModel == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userModel);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        if (page > 0) {
            page -= 1;
        }

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserModel> usersPage = userRepository.findAll(pageableRequest);
        List<UserModel> users = usersPage.getContent();

        for (UserModel userModel : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userModel, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }
}
