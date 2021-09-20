package com.exams.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AnswerDAO {
    void setAnswer(int id, String answer);
}
