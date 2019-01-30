package com.abaiyat.triagews.ui.controller;

import com.abaiyat.triagews.exceptions.UserServiceException;
import com.abaiyat.triagews.service.AddressService;
import com.abaiyat.triagews.service.UserService;
import com.abaiyat.triagews.shared.dto.AddressDTO;
import com.abaiyat.triagews.shared.dto.UserDto;
import com.abaiyat.triagews.ui.model.request.UserDetailsRequestModel;
import com.abaiyat.triagews.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final AddressService addressesService;

    @Autowired
    public UserController(UserService userService, AddressService addressesService) {
        this.userService = userService;
        this.addressesService = addressesService;
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
        if (userDetails.getName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        if (userDetails.getName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName("DELETE");

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }


    @GetMapping(path = "/{id}/addresses")
    public List<AddressesRest> getUserAddresses(@PathVariable String id) {
        List<AddressesRest> returnValue = new ArrayList<>();

        List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            returnValue = modelMapper.map(addressesDTO, listType);
        }

        return returnValue;
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}")
    public AddressesRest getUserAddressById(@PathVariable String userId, @PathVariable String addressId) {
        AddressesRest returnValue = new AddressesRest();

        AddressDTO addressDTO = addressesService.getAddressById(userId, addressId);

        if (addressDTO != null) {
            ModelMapper modelMapper = new ModelMapper();
            returnValue = modelMapper.map(addressDTO, AddressesRest.class);
        }

        return returnValue;
    }


}
