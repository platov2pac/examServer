package com.exams.service.exam;

import com.exams.dto.Answer;
import com.exams.dto.Exam;

import java.util.List;

public interface ExamService {
    Exam getExamByLogin(String token);

    List<Exam> getAllExam();

    void updateExam(String question, String examDate, int examId);

    void setAnswerByStudent(String token, String answer);

    Answer getAnswerByUserId(int id);

    Answer getAnswer(String token);

    void setGrade(int id, int grade);

    void setFinish(String token);

}
