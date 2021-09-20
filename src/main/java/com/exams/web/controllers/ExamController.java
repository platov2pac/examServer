package com.exams.web.controllers;

import com.exams.config.jwt.JwtFilter;
import com.exams.config.jwt.JwtProvide;
import com.exams.dao.AnswerDAO;
import com.exams.dao.ExamDAO;
import com.exams.dao.UserDAO;
import com.exams.dto.Exam;
import com.exams.dto.User;
import com.exams.service.error.processing.NotFoundException;
import com.exams.web.requests.GetExamReq;
import com.exams.web.requests.SetAnswerReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ExamController {
    @Autowired
    private ExamDAO examDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AnswerDAO answerDAO;

    @GetMapping("/getExam")
    public Exam getExam(@RequestBody GetExamReq req) {

        User user = userDAO.getByLogin(req.getLogin());
        if (user == null) {
            throw new NotFoundException();
        }
        Exam exam = examDAO.getExamByFacultyId(user.getFaculty().getId());
        if (exam == null) {
            throw new NotFoundException();
        }
        return exam;
    }

    @PostMapping("/setAnswer")
    public void setAnswer(@RequestBody SetAnswerReq req) {
        User user = userDAO.getByLogin(req.getLogin());
        if (user == null) {
            throw new NotFoundException("Некому записать ответ, т.к. пользователь не найден");
        }
        answerDAO.setAnswer(user.getId(), req.getAnswer());
    }
}
