package com.TheMFG.backend.dto;

import com.TheMFG.backend.enums.Audience;
import com.TheMFG.backend.enums.ReplyRestriction;
import com.TheMFG.backend.model.Image;
import com.TheMFG.backend.model.Poll;
import com.TheMFG.backend.model.Post;
import com.TheMFG.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDTO {
    private String content;
    private User author;
    Set<Post> replies;
    private List<Image> images;
    private Boolean scheduled;
    private Date scheduledDate;
    private Audience audience;
    private ReplyRestriction replyRestriction;
    private Poll poll;
}
