package com.exams.dao;

import com.exams.dto.Answer;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AnswerDAO {
    void setAnswer(int id, String answer);

    Answer getAnswerByUserId(int userId);

    void setGrade(int userId, int grade);
}
