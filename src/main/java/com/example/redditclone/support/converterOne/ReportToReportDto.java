package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Report;
import com.example.redditclone.models.User;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.ReportDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ReportToReportDto implements Converter<Report, ReportDto> {

    @Autowired
    private Converter<User, UserDto> toUserDto;
    @Autowired
    private Converter<Comment, CommentDto> toCommentDto;
    @Autowired
    private Converter<Post, PostDto> toPostDto;

    public ReportToReportDto() {}

    public ReportDto convert(Report source) {
        ReportDto dto = new ReportDto();
        dto.setReportID(source.getReportID());
        dto.setReportReason(source.getReportReason());
        dto.setTimestamp(source.getTimestamp());
        dto.setAccepted(source.isAccepted());
        dto.setComment(toCommentDto.convert(source.getComment()));
        dto.setPost(toPostDto.convert(source.getPost()));
        dto.setUser(toUserDto.convert(source.getUser()));
        return dto;
    }

    public List<ReportDto> convert(List<Report> source) {
        List<ReportDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Report s = (Report) var3.next();
            ReportDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
