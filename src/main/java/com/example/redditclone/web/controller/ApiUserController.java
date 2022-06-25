package com.example.redditclone.web.controller;

import com.example.redditclone.enums.UserRole;
import com.example.redditclone.security.TokenUtils;
import com.example.redditclone.models.User;
import com.example.redditclone.service.EmailService;
import com.example.redditclone.service.UserService;
import com.example.redditclone.web.dto.AuthDto;
import com.example.redditclone.web.dto.UserDto;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"api/users"})
public class ApiUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Converter<List<User>, List<UserDto>> toDtoUser;
    @Autowired
    private Converter<User, UserDto> toDto;
    @Autowired
    private Converter<UserDto, User> toUser;
    @Autowired
    private EmailService emailService;

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> users = userService.all();
        List<UserDto> body = toDtoUser.convert(users);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") Long id) {
        Optional<User> user = Optional.of(userService.one(id).orElse(new User()));
        UserDto userDto = toDto.convert(user.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/byUsername/{username}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable("username") String username) {
        Optional<User> user = userService.findByUsername(username);
        UserDto userDto = toDto.convert(user.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto,@PathVariable("id") Long id) {
        if (!id.equals(userDto.getUserID())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            User persisted = this.userService.save(toUser.convert(userDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PatchMapping("/block/{username}")
    public ResponseEntity<UserDto> blockUser(@PathVariable("username") String username) {
        Optional<User> persisted = userService.findByUsername(username);
        if(persisted.isPresent()) {
            persisted.get().setBanned(true);
            userService.save(persisted.get());
        }
        return persisted.map(user -> new ResponseEntity<>(toDto.convert(user), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/unblock/{username}")
    public ResponseEntity<UserDto> unblockUser(@PathVariable("username") String username) {
        Optional<User> persisted = userService.findByUsername(username);
        if(persisted.isPresent()) {
            persisted.get().setBanned(false);
            userService.save(persisted.get());
        }
        return persisted.map(user -> new ResponseEntity<>(toDto.convert(user), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/removeModerator/{username}")
    public ResponseEntity<UserDto> removeModerator(@PathVariable("username") String username) {
        Optional<User> persisted = userService.findByUsername(username);
        if(persisted.isPresent()) {
            persisted.get().setRole(UserRole.korisnik);
            userService.save(persisted.get());
        }
        return persisted.map(user -> new ResponseEntity<>(toDto.convert(user), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND));
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        User saved = userService.save(toUser.convert(userDto));
        return saved == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(toDto.convert(saved), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDto userDto) {

        Optional<User> korisnik = userService.findByUsername(userDto.getUsername());
        if (!korisnik.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
            return ResponseEntity.ok(tokenUtils.generateToken(userDetails));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ObjectNode objectNode) {
        Optional<User> user = userService.findByUsername(objectNode.get("username").asText());

        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if(!passwordEncoder.matches(objectNode.get("currentPassword").asText(), user.get().getPassword())) {
            return ResponseEntity.noContent().build();
        }

        user.get().setPassword(passwordEncoder.encode(objectNode.get("newPassword").asText()));

        userService.save(user.get());

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) throws MessagingException {

        Optional<User> user = userService.findByUsername(userDto.getUsername());
        if(user.isPresent()) return new ResponseEntity<>(toDto.convert(user.get()), HttpStatus.BAD_REQUEST);

        User saved = new User();

        saved.setUsername(userDto.getUsername());
        saved.setPassword(passwordEncoder.encode(userDto.getPassword()));
        saved.setEmail(userDto.getEmail());
        saved.setAvatar(userDto.getAvatar());
        saved.setBanned(false);
        saved.setProfileDescription("");
        saved.setDisplayName("");
        saved.setRole(UserRole.korisnik);
        saved.setRegistrationDate(LocalDateTime.now());

        userService.save(saved);

        emailService.sendMail(userDto.getEmail(), "Register Successfully", "You are now registered user, enjoy!");

        return new ResponseEntity<>(toDto.convert(saved), HttpStatus.CREATED);
    }

}
