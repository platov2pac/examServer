package com.exams.web.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReqUpdateEx {
    private String question;
    private String examDate;
    private int examId;
}
