package com.example.redditclone.service.impl;

import com.example.redditclone.models.User;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.service.UserService;
import com.example.redditclone.web.dto.UserPasswordChangeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class JpaUserService implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public JpaUserService() {
    }

    public Optional<User> one(Long id) {
        return this.userRepository.findById(id);
    }

    public List<User> all() {
        return this.userRepository.findAll();
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public void delete(Long id) { this.userRepository.deleteById(id); }

    public Optional<User> findByUsername(String username) {
        return this.userRepository.findFirstByUsername(username);
    }

    public boolean changePassword(Long id, UserPasswordChangeDto userPasswordChangeDto) {
        Optional<User> rezultat = this.userRepository.findById(id);
        if (!rezultat.isPresent()) {
            throw new EntityNotFoundException();
        } else {
            User user = rezultat.get();
            if (user.getUsername().equals(userPasswordChangeDto.getUsername()) && user.getPassword().equals(userPasswordChangeDto.getPassword())) {
                String password = userPasswordChangeDto.getPassword();
                if (!userPasswordChangeDto.getPassword().equals("")) {
                    password = this.passwordEncoder.encode(userPasswordChangeDto.getPassword());
                }

                user.setPassword(password);
                this.userRepository.save(user);
                return true;
            } else {
                return false;
            }
        }
    }
}
