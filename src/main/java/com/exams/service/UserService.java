package com.exams.service;

import com.exams.dao.UserDAO;
import com.exams.dto.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    User getByLogin(String login);

    List<User> getEnteredUserFromFaculty();
}
