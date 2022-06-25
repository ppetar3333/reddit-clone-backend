package com.example.redditclone.web.controller;

import com.example.redditclone.models.Banned;
import com.example.redditclone.models.User;
import com.example.redditclone.service.BannedService;
import com.example.redditclone.web.dto.BannedDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"api/banned"})
public class ApiBannedController {

    @Autowired
    private BannedService bannedService;
    @Autowired
    private Converter<List<Banned>, List<BannedDto>> toDtoBanned;
    @Autowired
    private Converter<Banned, BannedDto> toDto;
    @Autowired
    private Converter<BannedDto, Banned> toBanned;
    @Autowired
    private Converter<UserDto, User> toDtoUser;

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<BannedDto>> getAll() {
        List<Banned> comments = bannedService.all();
        List<BannedDto> body = toDtoBanned.convert(comments);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<BannedDto> getById(@PathVariable("id") Long id) {
        Optional<Banned> banned = Optional.of(bannedService.one(id).orElse(new Banned()));
        BannedDto bannedDto = toDto.convert(banned.get());
        return new ResponseEntity<>(bannedDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        bannedService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BannedDto> update(@RequestBody BannedDto bannedDto, @PathVariable("id") Long id) {
        if (!id.equals(bannedDto.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Banned persisted = this.bannedService.save(toBanned.convert(bannedDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<BannedDto> save(@RequestBody BannedDto bannedDto) {

        Banned saved = new Banned();

        saved.setTimestamp(LocalDateTime.now());
        saved.setByModerator(toDtoUser.convert(bannedDto.getByModerator()));

        bannedService.save(saved);

        return new ResponseEntity<>(toDto.convert(saved), HttpStatus.CREATED);
    }
}
