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

    List<User> getEnteredUserFromFaculty(String token);

    void registerNewUser(User user) throws BadRequestException;

    List<User> getAnsweredUserFromFaculty(String token);

    List<User> getNotAnsweredUserFromFaculty(String token);

    List<User> getProfessors();

    void updateUser(User user, String oldLogin);

    void addProfessor(User user);

    void deleteUser(int userId);
}
