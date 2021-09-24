package com.exams.service.exam;

import com.exams.config.jwt.JwtProvide;
import com.exams.dao.AnswerDAO;
import com.exams.dao.ExamDAO;
import com.exams.dao.UserDAO;
import com.exams.dto.Answer;
import com.exams.dto.Exam;
import com.exams.dto.User;
import com.exams.service.error.processing.BadRequestException;
import com.exams.service.error.processing.NotFoundException;
import com.exams.service.user.UserService;
import com.exams.web.requests.ReqUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamDAO examDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvide jwtProvide;

    @Override
    public Exam getExamByLogin(String token) throws NotFoundException {
        String login = jwtProvide.getLoginFromToken(token);
        User user = userDAO.getByLogin(login);
        if (user == null) {
            throw new NotFoundException("Нет пользователя, которому нужно отдать экзамен");
        }
        Exam exam = examDAO.getExamByFacultyId(user.getFaculty().getId());
        if (exam == null) {
            throw new NotFoundException("Нет экзамена для выбранного пользовтеля");
        }
        return exam;
    }

    @Override
    public void setAnswerByStudent(String token, String answer) throws NotFoundException {
        String login = jwtProvide.getLoginFromToken(token);
        User user = userDAO.getByLogin(login);
        if (user == null) {
            throw new NotFoundException("Некому записать ответ, т.к. пользователь не найден");
        }
        answerDAO.setAnswer(user.getId(), answer);
    }

    @Override
    public Answer getAnswerByUserId(int id) {
        Answer answer = answerDAO.getAnswerByUserId(id);
        if (answer == null) {
            throw new NotFoundException("Нет ответа,для выбранного пользователя");
        }
        return answer;
    }

    @Override
    public Answer getAnswer(String token) {
        String login = jwtProvide.getLoginFromToken(token);
        User user = userDAO.getByLogin(login);
        Answer answer = answerDAO.getAnswerByUserId(user.getId());
        if (answer == null) {
            throw new NotFoundException("Нет ответа,для выбранного пользователя");
        }
        return answer;
    }

    @Override
    public void setGrade(int id, int grade) {
        answerDAO.setGrade(id, grade);
    }

    @Override
    public void setFinish(String token) {
        String login = jwtProvide.getLoginFromToken(token);
        User user = userDAO.getByLogin(login);
        if (user == null) {
            throw new BadRequestException("Не верный логин в запросе");
        }
        List<User> notCheckedUser = userService.getAnsweredUserFromFaculty(token);
        if (!notCheckedUser.isEmpty()) {
            throw new BadRequestException("Проверены не все пользователи, чтобы завершить экзамен");
        }
        examDAO.setFinish(user.getFaculty().getId());
    }
}
