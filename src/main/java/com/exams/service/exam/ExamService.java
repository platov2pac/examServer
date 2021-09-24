package com.exams.service.exam;

import com.exams.dto.Answer;
import com.exams.dto.Exam;

public interface ExamService {
    Exam getExamByLogin(String token);

    void setAnswerByStudent(String token, String answer);

    Answer getAnswerByUserId(int id);

    Answer getAnswer(String token);

    void setGrade(int id, int grade);

    void setFinish(String token);

}
