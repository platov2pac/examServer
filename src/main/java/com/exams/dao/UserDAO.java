package com.exams.dao;

import com.exams.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDAO {
    User getByLogin(String login);
    List<User> getEnteredUserFromFaculty();
}
