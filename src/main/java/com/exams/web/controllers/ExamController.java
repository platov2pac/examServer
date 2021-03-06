package com.exams.web.controllers;

import com.exams.dto.Answer;
import com.exams.dto.Exam;
import com.exams.service.error.processing.BadRequestException;
import com.exams.service.error.processing.NotFoundException;
import com.exams.service.exam.ExamService;
import com.exams.web.requests.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200/")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping("/getExam")
    public Exam getExam(@RequestHeader("Authorization") String token) {
        try {
            return examService.getExamByLogin(token.substring(7));
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getMessage());
        } catch (BadRequestException bre) {
            throw new BadRequestException(bre.getMessage());
        }
    }

    @PostMapping("/student/setAnswer")
    public void setAnswer(@RequestBody SetAnswerReq req, @RequestHeader("Authorization") String token) {
        try {
            examService.setAnswerByStudent(token.substring(7), req.getAnswer());
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getMessage());
        }

    }

    @GetMapping("/professor/getAnswer")
    public Answer getAnswer(@RequestParam Integer id) {
        try {
            return examService.getAnswerByUserId(id);
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getMessage());
        }
    }

    @GetMapping("/student/getAnswer")
    public Answer getAnswer(@RequestHeader("Authorization") String token) {
        try {
            return examService.getAnswer(token.substring(7));
        } catch (NotFoundException nfe) {
            throw new NotFoundException(nfe.getMessage());
        }
    }

    @PostMapping("/professor/setGrade")
    public void setGrade(@RequestBody ReqGrade req) {
        if (req.getUserId() == 0) {
            throw new BadRequestException("user id ???? ?????? ??????????????");
        }
        examService.setGrade(req.getUserId(), req.getGrade());
    }

    @PostMapping("/professor/finishExam")
    public void setFinishExam(@RequestHeader("Authorization") String token) {
        try {
            examService.setFinish(token.substring(7));
        } catch (BadRequestException bre) {
            throw new BadRequestException(bre.getMessage());
        }
    }

    @GetMapping("/admin/getExam")
    public List<Exam> getAllExam() {
        return examService.getAllExam();
    }

    @PostMapping("/admin/updateExam")
    public void updateExam(@RequestBody ReqUpdateEx req) {
        examService.updateExam(req.getQuestion(), req.getExamDate(), req.getExamId());
    }
}
