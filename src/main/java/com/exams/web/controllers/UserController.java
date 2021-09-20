package com.exams.web.controllers;

import com.exams.config.PasswordEncoder;
import com.exams.config.jwt.JwtProvide;
import com.exams.dto.User;
import com.exams.service.error.processing.NotFoundException;
import com.exams.service.user.UserService;
import com.exams.service.error.processing.BadRequestException;
import com.exams.web.requests.AuthReq;
import com.exams.web.responses.AuthResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("jwtProvide")
    private JwtProvide jwtProvide;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/getUser")
    private User getUser(@RequestBody String login) {
        User user = userService.getByLogin(login);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @GetMapping("/login")
    private AuthResp login(@RequestBody AuthReq authReq) {
        User user = userService.getByLogin(authReq.getLogin());
        AuthResp authResp = null;
        if (user == null) {
            throw new NotFoundException();
        }
        if (passwordEncoder.matches(authReq.getPassword(), user.getPassword())) {
            String token = jwtProvide.generateToken(user.getLogin());
            authResp = new AuthResp(user.getLogin(), user.getRole().getName(), token);

        }
        if (authResp == null) {
            throw new NotFoundException();
        }
        return authResp;
    }

    @PostMapping("/registration")
    private void registration(@RequestBody User newUser) {
        try {
            userService.registerNewUser(newUser);
        } catch (BadRequestException be) {
            throw new BadRequestException("Экзамен уже начался");
        }
    }

}
