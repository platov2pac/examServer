package com.exams.service;

import com.exams.dao.UserDAO;
import com.exams.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAOBatis;

    @Override
    public User getByLogin(String login) {
        return userDAOBatis.getByLogin(login);
    }

    @Override
    public List<User> getEnteredUserFromFaculty() {
        return null;
    }
}
