package com.example.redditclone.web.controller;

import com.example.redditclone.service.elasticsearch.ReaderService;
import com.example.redditclone.web.dto.ReaderDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reader")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping
    public void addReader(@RequestBody ReaderDTO readerDTO){
        readerService.index(readerDTO);
    }

    @GetMapping
    public List<ReaderDTO> readersByFirstNameLastName(@RequestParam(name = "name") String firstName, @RequestParam(name = "lastName") String lastName) {
        return readerService.findByFirstNameAndLastName(firstName, lastName);
    }

}
