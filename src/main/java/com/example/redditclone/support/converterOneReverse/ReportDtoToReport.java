package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Report;
import com.example.redditclone.models.User;
import com.example.redditclone.service.ReportService;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.ReportDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReportDtoToReport implements Converter<ReportDto, Report> {

    @Autowired
    private ReportService reportService;
    @Autowired
    private Converter<UserDto, User> toDtoUser;
    @Autowired
    private Converter<CommentDto, Comment> toDtoComment;
    @Autowired
    private Converter<PostDto, Post> toDtoPost;

    public ReportDtoToReport() { }

    public Report convert(ReportDto source) {
        Report target = null;

        if (source.getReportID() != null) target = reportService.one(source.getReportID()).get();

        if (target == null) target = new Report();

        target.setReportID(source.getReportID());
        target.setReportReason(source.getReportReason());
        target.setTimestamp(source.getTimestamp());
        target.setAccepted(source.isAccepted());
        target.setUser(toDtoUser.convert(source.getUser()));
        target.setComment(toDtoComment.convert(source.getComment()));
        target.setPost(toDtoPost.convert(source.getPost()));

        return target;
    }
}
