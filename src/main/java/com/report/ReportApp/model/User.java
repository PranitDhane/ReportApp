package com.report.ReportApp.model;

import lombok.*;

@Getter
@Setter
@ToString
public class User {
    public User(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;

    }

    private String userId;
    private String email;
    private String name;
}
