package com.example.redditclone.service.impl;

import com.example.redditclone.models.Report;
import com.example.redditclone.repository.ReportRepository;
import com.example.redditclone.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaReportService implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Optional<Report> one(Long id) {
        return reportRepository.findById(id);
    }

    public List<Report> all() {
        return reportRepository.findAll();
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    public void delete(Long id) { reportRepository.deleteById(id); }

    @Override
    public Report getReportByPostPostID(Long id) {
        return reportRepository.getReportByPostPostID(id);
    }

    @Override
    public Report getReportByCommentCommentID(Long id) {
        return reportRepository.getReportByCommentCommentID(id);
    }
}
