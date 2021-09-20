package com.exams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    private int id;
    private int idFaculty;
    private Date examDate;
    private String question;
}
