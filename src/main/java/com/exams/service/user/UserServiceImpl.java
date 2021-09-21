package com.exams.service.user;

import com.exams.config.PasswordEncoder;
import com.exams.config.jwt.JwtProvide;
import com.exams.dao.ExamDAO;
import com.exams.dao.UserDAO;
import com.exams.dto.User;
import com.exams.service.error.processing.BadRequestException;
import com.exams.service.error.processing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAOBatis;

    @Autowired
    private ExamDAO examDAOBatis;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvide jwtProvide;

    @Override
    public User getByLogin(String login) {
        return userDAOBatis.getByLogin(login);
    }

    @Override
    public List<User> getEnteredUserFromFaculty() {
        return null;
    }

    @Override
    public void registerNewUser(User user) throws BadRequestException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int facultyId = user.getFaculty().getId();
        Date examDate = examDAOBatis.getExamByFacultyId(facultyId).getExamDate();
        Date currentDate = new Date(System.currentTimeMillis());
        if (examDate.before(currentDate)) {
            throw new BadRequestException();
        }
        userDAOBatis.addUser(user);
    }

    @Override
    public List<User> getAnsweredUserFromFaculty(String token) throws NotFoundException {
        String login = jwtProvide.getLoginFromToken(token);
        User user = userDAOBatis.getByLogin(login);
        if (user == null) {
            throw new NotFoundException("Преподователя нет");
        }
        return userDAOBatis.getAnsweredUsersFromFaculty(user.getFaculty().getId());
    }
}
