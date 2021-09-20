package com.exams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    private int id;
    private int idUser;
    private int idExams;
    private String value;
    private int grade;
    private boolean status;
}
