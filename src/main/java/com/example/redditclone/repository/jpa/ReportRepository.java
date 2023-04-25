package com.example.redditclone.repository.jpa;

import com.example.redditclone.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report getReportByPostPostID(Long id);
    Report getReportByCommentCommentID(Long id);
}
