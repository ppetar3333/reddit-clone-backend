package com.example.redditclone.web.dto;

import com.example.redditclone.enums.ReportReason;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Report;
import com.example.redditclone.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReportDto implements Serializable {

    @NotNull
    private Long reportID;
    @NotNull
    private ReportReason reportReason;
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private boolean accepted;
    @NotNull
    private CommentDto comment;
    @NotNull
    private PostDto post;
    @NotNull
    private UserDto user;
    @JsonIgnore
    @Autowired
    private Converter<Comment, CommentDto> toDtoComment;
    @JsonIgnore
    @Autowired
    private Converter<Post, PostDto> toDtoPost;
    @JsonIgnore
    @Autowired
    private Converter<User, UserDto> toDtoUser;

    public ReportDto() {}

    public ReportDto(Report report) {
        this.reportID = report.getReportID();
        this.reportReason = report.getReportReason();
        this.timestamp = report.getTimestamp();
        this.accepted = report.isAccepted();
        if(report.getComment() != null) this.comment = toDtoComment.convert(report.getComment());
        if(report.getPost() != null) this.post = toDtoPost.convert(report.getPost());
        if(report.getUser() != null) this.user = toDtoUser.convert(report.getUser());
    }
}
