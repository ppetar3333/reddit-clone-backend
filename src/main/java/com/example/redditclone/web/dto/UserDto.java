package com.example.redditclone.web.dto;

import com.example.redditclone.enums.UserRole;
import com.example.redditclone.models.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserDto implements Serializable {

    private Long userID;
    @Size(min = 5, max = 20, message = "Username needs to be between 5 and 20")
    private String username;
    @Size(min = 5, max = 20, message = "Username needs to be between 5 and 20")
    private String password;
    @Size(min = 8, max = 50, message = "Email needs to be between 8 and 50")
    private String email;
    private String avatar;
    private String profileDescription;
    @NotNull
    private boolean banned;
    @NotNull
    private String displayName;
    @NotNull
    private LocalDateTime registrationDate;
    @NotNull
    private UserRole role;

    public UserDto() {}

    public UserDto(User user){
        this.userID = user.getUserID();
        this.username= user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.banned = user.isBanned();
        this.profileDescription = user.getProfileDescription();
        this.displayName = user.getDisplayName();
        this.role = user.getRole();
        this.registrationDate = user.getRegistrationDate();
    }
}
