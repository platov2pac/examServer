package com.exams.dao;

import com.exams.dto.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@Mapper
public interface ExamDAO {
    Exam getExamByFacultyId(int facultyId);

    List<Exam> getAllExam();

    void updateExam(String question, String examDate, int examId);

    void setFinish(int facultyId);
}
