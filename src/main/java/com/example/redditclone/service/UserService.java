package com.example.redditclone.service;

import com.example.redditclone.models.User;
import com.example.redditclone.web.dto.UserPasswordChangeDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface UserService {

    Optional<User> one(Long id);

    List<User> all();

    User save(User user);

    void delete(Long id);

    Optional<User> findByUsername(String username);

    boolean changePassword(Long id, UserPasswordChangeDto userPasswordChangeDto);
}
