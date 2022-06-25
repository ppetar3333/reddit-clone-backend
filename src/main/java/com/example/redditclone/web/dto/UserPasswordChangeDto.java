package com.example.redditclone.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class UserPasswordChangeDto {
    private String username;
    private String oldPassword;
    @Pattern(regexp = "^(?=.*[A-Z].*[A-Z])(?=.*[!@#$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}$")
    private String password;
    private String passwordConfirm;
}
