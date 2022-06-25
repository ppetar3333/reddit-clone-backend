package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.User;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class UserToUserDto implements Converter<User, UserDto> {

    public UserToUserDto() {}

    public UserDto convert(User source) {
        UserDto dto = new UserDto();
        if (source != null) {
            dto.setUserID(source.getUserID());
            dto.setUsername(source.getUsername());
            dto.setPassword(source.getPassword());
            dto.setRegistrationDate(source.getRegistrationDate());
            dto.setEmail(source.getEmail());
            dto.setAvatar(source.getAvatar());
            dto.setBanned(source.isBanned());
            dto.setDisplayName(source.getDisplayName());
            dto.setProfileDescription(source.getProfileDescription());
            dto.setRole(source.getRole());
        }
        return dto;
    }

    public List<UserDto> convert(List<User> source) {
        List<UserDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            User s = (User) var3.next();
            UserDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
