package com.example.redditclone.support.converterList;

import com.example.redditclone.models.User;
import com.example.redditclone.support.converterOne.UserToUserDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class UserListToUserListDto implements Converter<List<User>, List<UserDto>> {

    @Autowired
    private UserToUserDto toDto;

    public UserListToUserListDto() { }

    public List<UserDto> convert(List<User> source) {
        List<UserDto> target = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            User u = (User)var3.next();
            UserDto dto = this.toDto.convert(u);
            target.add(dto);
        }

        return target;
    }

}
