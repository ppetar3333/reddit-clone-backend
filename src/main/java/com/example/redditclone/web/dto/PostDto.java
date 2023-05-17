package com.example.redditclone.web.dto;

import com.example.redditclone.models.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostDto implements Serializable {

    @NotNull
    private Long postID;
    @NotEmpty(message = "Title can't be empty")
    @Size(min = 5, max = 100, message = "Title needs to be between 5 and 100")
    private String title;
    @NotEmpty(message = "Text can't be empty")
    @Size(min = 5, max = 100000000, message = "Title nedds to be between 5 and 1000")
    private String text;
    @NotNull
    private LocalDateTime creationDate;
    private String imagePath;
    @NotNull
    private int voteCount;
    private String textFromPdf;
    @NotNull
    private UserDto user;
    private FlairDto flair;
    private SubredditDto subreddit;
    @JsonIgnore
    @Autowired
    private Converter<User, UserDto> toDtoUser;
    @JsonIgnore
    @Autowired
    private Converter<Flair, FlairDto> toDtoFlair;
    @JsonIgnore
    @Autowired
    private Converter<Subreddit, SubredditDto> toDtoSubreddit;
    private MultipartFile[] files;

    public PostDto() {}

    public PostDto(Post post) {
        this.postID = post.getPostID();
        this.title = post.getTitle();
        this.text = post.getText();
        this.voteCount = post.getVoteCount();
        this.creationDate = post.getCreationDate();
        this.imagePath = post.getImagePath();
        this.user = toDtoUser.convert(post.getUser());
        this.textFromPdf = post.getTextFromPdf();
        if(post.getFlair() != null) this.flair = toDtoFlair.convert(post.getFlair());
        if(post.getSubreddit() != null) this.subreddit = toDtoSubreddit.convert(post.getSubreddit());
    }
}
