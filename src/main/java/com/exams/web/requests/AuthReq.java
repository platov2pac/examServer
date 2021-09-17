package com.exams.web.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthReq {
    private String login;
    private String password;
}
