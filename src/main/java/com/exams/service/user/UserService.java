package com.exams.service.user;

import com.exams.dao.UserDAO;
import com.exams.dto.User;
import com.exams.service.error.processing.BadRequestException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public interface UserService {
    User getByLogin(String login);

    List<User> getEnteredUserFromFaculty();

    void registerNewUser(User user) throws BadRequestException;
}
