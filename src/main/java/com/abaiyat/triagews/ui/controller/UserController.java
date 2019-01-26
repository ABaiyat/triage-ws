package com.abaiyat.triagews.ui.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping
    public String getUser() {
        return "Get User was called!";
    }

    @PostMapping
    public String createUser() {
        return "Create User was called!";
    }

    @PutMapping
    public String updateUser() {
        return "Update User was called!";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete User was called!";
    }
}
