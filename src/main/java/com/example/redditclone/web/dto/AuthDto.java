package com.example.redditclone.web.dto;

import com.example.redditclone.models.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class AuthDto implements Serializable {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public AuthDto() {}

    public AuthDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}
