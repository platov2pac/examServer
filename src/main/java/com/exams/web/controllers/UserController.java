package com.exams.web.controllers;

import com.exams.config.jwt.JwtProvide;
import com.exams.dto.User;
import com.exams.service.UserService;
import com.exams.web.requests.AuthReq;
import com.exams.web.responses.AuthResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvide jwtProvide;

    @Bean
    private BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping("/getUser")
    private User doPost(@RequestParam String login) {
        return userService.getByLogin(login);
    }

    @PostMapping("/login")
    private AuthResp doPost(@RequestBody AuthReq authReq) {
        User user = userService.getByLogin(authReq.getLogin());
        if (user != null) {
            if (bCryptPasswordEncoder().matches(user.getPassword(), authReq.getPassword())) {
                String token = jwtProvide.generateToken(user.getLogin());
                AuthResp authResp = new AuthResp(user.getLogin(), user.getRole().getName(), token);
                return authResp;
            }
        }
        return null;
    }
}
