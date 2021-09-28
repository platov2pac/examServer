package com.exams.service.user;

import com.exams.config.PasswordEncoder;
import com.exams.config.jwt.JwtProvide;
import com.exams.dao.AnswerDAO;
import com.exams.dao.ExamDAO;
import com.exams.dao.UserDAO;
import com.exams.dto.Answer;
import com.exams.dto.Exam;
import com.exams.dto.Role;
import com.exams.dto.User;
import com.exams.service.error.processing.BadRequestException;
import com.exams.service.error.processing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAOBatis;

    @Autowired
    private ExamDAO examDAOBatis;

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvide jwtProvide;

    @Override
    public User getByLogin(String login) {
        return userDAOBatis.getByLogin(login);
    }

    @Override
    public List<User> getEnteredUserFromFaculty(String token) {
        String login = jwtProvide.getLoginFromToken(token.substring(7));
        int facultyId = userDAOBatis.getByLogin(login).getFaculty().getId();
        Exam exam = examDAOBatis.getExamByFacultyId(facultyId);
        List<User> checkedUsers = userDAOBatis.getCheckedUserFromFaculty(facultyId);
        int sumGrades = 0;
        double avGrade = 0.0;
        if (!checkedUsers.isEmpty() && exam.isFinish()) {
            sumGrades = checkedUsers.stream().map(user ->
                    answerDAO.getAnswerByUserId(user.getId())).mapToInt(Answer::getGrade).sum();
        } else {
            throw new NotFoundException("Экзамен еще идёт или не начинался");
        }
        avGrade = (double) sumGrades / checkedUsers.size();
        List<User> enteredUsers = new ArrayList<>();
        double finalAvGrade = avGrade;
        checkedUsers.forEach(user -> {
            int grade = answerDAO.getAnswerByUserId(user.getId()).getGrade();
            if (grade >= finalAvGrade) {
                enteredUsers.add(user);
            }
        });
        return enteredUsers;
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
        User newUser = userDAOBatis.getByLogin(user.getLogin());
        if (newUser.getRole().getId() == 3) {
            answerDAO.regAnswer(newUser.getId());
        }
    }

    @Override
    public List<User> getAnsweredUserFromFaculty(String token) throws NotFoundException {
        String login = jwtProvide.getLoginFromToken(token);
        User user = userDAOBatis.getByLogin(login);
        if (user == null) {
            throw new NotFoundException("Преподователя нет");
        }
        Date examDate = examDAOBatis.getExamByFacultyId(user.getFaculty().getId()).getExamDate();
        Date currentDate = new Date(System.currentTimeMillis());
        if (examDate.after(currentDate)) {
            throw new BadRequestException("Экзамен не начался");
        }
        return userDAOBatis.getAnsweredUsersFromFaculty(user.getFaculty().getId());
    }

    @Override
    public List<User> getNotAnsweredUserFromFaculty(String token) {
        String login = jwtProvide.getLoginFromToken(token);
        User user = userDAOBatis.getByLogin(login);
        if (user == null) {
            throw new NotFoundException("Пользователя нет");
        }
        return userDAOBatis.getNotAnsweredUsersFromFaculty(user.getFaculty().getId());
    }

    @Override
    public List<User> getProfessors() {
        return userDAOBatis.getProfessors();
    }

    @Override
    public void updateUser(User user, String oldLogin) {
        User existUser = userDAOBatis.getByLogin(oldLogin);
        User repetitiveUser = userDAOBatis.getByLogin(user.getLogin());
        if (existUser == null) {
            throw new BadRequestException("Вы пытаетесь обновить не существующего пользователя");
        }
        if (repetitiveUser != null && !oldLogin.equals(user.getLogin())) {
            throw new BadRequestException("Логин уже занят");
        }
        if(user.getPassword()==null || user.getPassword().equals("")){
            throw new BadRequestException("Пароль пуст");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAOBatis.updateUser(user, oldLogin);
    }

    @Override
    public void addProfessor(User user) {
        User repetitiveUser = userDAOBatis.getByLogin(user.getLogin());
        if (repetitiveUser != null) {
            throw new BadRequestException("Логин уже занят");
        }
        if(user.getPassword()==null || user.getPassword().equals("")){
            throw new BadRequestException("Пароль пуст");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(new Role(2, "ROLE_ADMIN"));
        userDAOBatis.addUser(user);
    }


    @Override
    public void deleteUser(int userId) {
        userDAOBatis.deleteUser(userId);
    }
}
