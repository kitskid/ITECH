package com.example.securityapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJs {
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;

    public UserJs(String email, String first_name, String last_name, String avatar) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAvatar() {
        return avatar;
    }
}
