package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.User;
import com.example.redditclone.service.UserService;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUser implements Converter<UserDto, User> {

    @Autowired
    private UserService userService;

    public UserDtoToUser() { }

    public User convert(UserDto source) {
        User target = null;

        if (source.getUserID() != null) target = userService.one(source.getUserID()).get();

        if (target == null) target = new User();

        target.setUserID(source.getUserID());
        target.setUsername(source.getUsername());
        target.setPassword(source.getPassword());
        target.setRegistrationDate(source.getRegistrationDate());
        target.setEmail(source.getEmail());
        target.setAvatar(source.getAvatar());
        target.setRegistrationDate(source.getRegistrationDate());
        target.setRole(source.getRole());
        target.setDisplayName(source.getDisplayName());
        target.setProfileDescription(source.getProfileDescription());
        target.setBanned(source.isBanned());

        return target;
    }
}
