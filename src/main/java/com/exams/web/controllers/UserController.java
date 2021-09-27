package com.exams.web.controllers;

import com.exams.config.PasswordEncoder;
import com.exams.config.jwt.JwtProvide;
import com.exams.dto.User;
import com.exams.service.error.processing.NotFoundException;
import com.exams.service.user.UserService;
import com.exams.service.error.processing.BadRequestException;
import com.exams.web.requests.AuthReq;
import com.exams.web.requests.ReqUserId;
import com.exams.web.requests.ReqWithLogin;
import com.exams.web.responses.AuthResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("jwtProvide")
    private JwtProvide jwtProvide;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/getUser")
    private User getUser(@RequestParam String login) {
        User user = userService.getByLogin(login);
        if (user == null) {
            throw new NotFoundException("Пользователь с таким логином не найден");
        }
        return user;
    }

    @PostMapping("/login")
    private AuthResp login(@RequestBody AuthReq authReq) {
        User user = userService.getByLogin(authReq.getLogin());
        AuthResp authResp = null;
        if (user == null) {
            throw new NotFoundException("Неверный логин");
        }
        if (passwordEncoder.matches(authReq.getPassword(), user.getPassword())) {
            String token = jwtProvide.generateToken(user.getLogin());
            authResp = new AuthResp(user.getLogin(), user.getRole().getName(), token);

        }
        if (authResp == null) {
            throw new NotFoundException("Неверный пароль");
        }
        return authResp;
    }

    @PostMapping("/registration")
    private void registration(@RequestBody User newUser) {
        if (userService.getByLogin(newUser.getLogin()) != null) {
            throw new BadRequestException("Пользователь с таким логином уже существует");
        }
        try {
            userService.registerNewUser(newUser);
        } catch (BadRequestException be) {
            throw new BadRequestException("Экзамен уже начался");
        }
    }

    @GetMapping("/professor/answeredList")
    private List<User> getAnsweredUsers(@RequestHeader("Authorization") String token) {
        try {
            return userService.getAnsweredUserFromFaculty(token.substring(7));
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getMessage());
        } catch (BadRequestException bre){
            throw new BadRequestException(bre.getMessage());
        }
    }

    @GetMapping("/professor/notAnsweredList")
    private List<User> getNotAnsweredUsers(@RequestHeader("Authorization") String token) {
        try {
            return userService.getNotAnsweredUserFromFaculty(token.substring(7));
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getMessage());
        }
    }

    @GetMapping("/getEnteredUsers")
    public List<User> getEnteredU(@RequestHeader("Authorization") String token) {
        try {
            return userService.getEnteredUserFromFaculty(token);
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getMessage());
        }
    }

    @PostMapping("/admin/updateUser")
    public void updateUser(@RequestBody User newUser, @RequestParam String oldLogin) {
        try {
            userService.updateUser(newUser, oldLogin);
        } catch (BadRequestException bre) {
            throw new BadRequestException(bre.getMessage());
        }
    }

    @DeleteMapping("/admin/deleteUser")
    public void deleteUser(@RequestBody ReqUserId req) {
        userService.deleteUser(req.getId());
    }
}
