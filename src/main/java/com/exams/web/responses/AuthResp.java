package com.exams.web.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResp {
    private String login;
    private String roleName;
    private String token;
}
