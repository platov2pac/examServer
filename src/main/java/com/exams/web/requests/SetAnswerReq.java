package com.exams.web.requests;

import lombok.Data;

@Data
public class SetAnswerReq {
    private String login;
    private String answer;
}
