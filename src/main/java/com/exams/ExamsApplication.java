package com.exams;

import com.exams.service.UserService;
import com.exams.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamsApplication.class, args);
    }

}
