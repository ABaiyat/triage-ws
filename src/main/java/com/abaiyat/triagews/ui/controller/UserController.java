package com.abaiyat.triagews.ui.controller;

import com.abaiyat.triagews.exceptions.UserServiceException;
import com.abaiyat.triagews.service.UserService;
import com.abaiyat.triagews.shared.dto.UserDto;
import com.abaiyat.triagews.ui.model.request.UserDetailsRequestModel;
import com.abaiyat.triagews.ui.model.response.ErrorMessages;
import com.abaiyat.triagews.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        if(userDetails.getName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser() {
        return "Update UserModel was called!";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete UserModel was called!";
    }
}
