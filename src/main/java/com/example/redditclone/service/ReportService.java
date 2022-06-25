package com.example.redditclone.service;

import com.example.redditclone.models.Report;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReportService {
    Optional<Report> one(Long id);

    List<Report> all();

    Report save(Report report);

    void delete(Long id);

    Report getReportByPostPostID(Long id);

    Report getReportByCommentCommentID(Long id);
}
