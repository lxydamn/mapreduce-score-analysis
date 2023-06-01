package com.example.backend.controller;


import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/user/login")
    public Map<String, String> login(@RequestParam Map<String, String> map) {

        String username = map.get("username");
        String password = map.get("password");

        return userService.login(username, password);
    }

    @PostMapping("/api/user/register")
    public Map<String, String> register(@RequestParam Map<String, String> map) {

        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");

        return userService.register(username, password, confirmedPassword);
    }


}
