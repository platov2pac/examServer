package com.exams.web.controllers;

import com.exams.dto.User;
import com.exams.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/getUser")
    private User doPost(@RequestParam String login) {
        return userService.getByLogin(login);
    }
}
