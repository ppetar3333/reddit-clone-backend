package com.example.redditclone.web.dto;

import com.example.redditclone.models.Flair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class FlairDto implements Serializable {

    @NotNull
    private Long flairID;
    @NotBlank(message = "Name of flair can't be blank")
    @Min(value = 3, message = "Min Length Is 3")
    @Max(value = 30, message = "Max Length Is 30")
    private String name;

    public FlairDto() {}

    public FlairDto(Flair flair) {
        if(flair != null) {
            this.flairID = flair.getFlairID();
            this.name = flair.getName();
        }
    }
}
