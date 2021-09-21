package com.exams.dao;

import com.exams.dto.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ExamDAO {
    Exam getExamByFacultyId(int facultyId);

    void setFinish(int facultyId);
}
