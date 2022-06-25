package com.example.redditclone.web.controller;

import com.example.redditclone.enums.ReportReason;
import com.example.redditclone.models.*;
import com.example.redditclone.service.ReportService;
import com.example.redditclone.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"api/reports"})
public class ApiReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private Converter<List<Report>, List<ReportDto>> toDtoReport;
    @Autowired
    private Converter<Report, ReportDto> toDto;
    @Autowired
    private Converter<ReportDto, Report> toReport;
    @Autowired
    private Converter<CommentDto, Comment> toComment;
    @Autowired
    private Converter<PostDto, Post> toPost;
    @Autowired
    private Converter<UserDto, User> toUser;

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<ReportDto>> getAll() {
        List<Report> reports = reportService.all();
        List<ReportDto> body = toDtoReport.convert(reports);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<ReportDto> getById(@PathVariable("id") Long id) {
        Optional<Report> report = Optional.of(reportService.one(id).orElse(new Report()));
        ReportDto reportDto = toDto.convert(report.get());
        return new ResponseEntity<>(reportDto, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<ReportDto> getReportsByPostID(@PathVariable("id") Long id) {
        Report report = reportService.getReportByPostPostID(id);
        ReportDto reportDto = toDto.convert(report);
        return new ResponseEntity<>(reportDto, HttpStatus.OK);
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<ReportDto> getReportsByCommentID(@PathVariable("id") Long id) {
        Report report = reportService.getReportByCommentCommentID(id);
        ReportDto reportDto = toDto.convert(report);
        return new ResponseEntity<>(reportDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reportService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReportDto> update(@RequestBody ReportDto reportDto, @PathVariable("id") Long id) {
        if (!id.equals(reportDto.getReportID())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Report persisted = this.reportService.save(toReport.convert(reportDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ReportDto> save(@RequestBody ReportDto reportDto) {
        Report report = new Report();
        
        report.setReportReason(reportDto.getReportReason());
        if(reportDto.getComment() != null) report.setComment(toComment.convert(reportDto.getComment()));
        if(reportDto.getPost() != null) report.setPost(toPost.convert(reportDto.getPost()));
        report.setUser(toUser.convert(reportDto.getUser()));
        report.setAccepted(reportDto.isAccepted());
        report.setTimestamp(LocalDateTime.now());

        this.reportService.save(report);

        return new ResponseEntity<>(toDto.convert(report), HttpStatus.CREATED);
    }
}
