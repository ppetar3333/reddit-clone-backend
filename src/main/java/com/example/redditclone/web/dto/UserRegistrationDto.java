package com.example.redditclone.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRegistrationDto {

    private String password;
    private String passwordConfirm;

    public UserRegistrationDto() {}
}
