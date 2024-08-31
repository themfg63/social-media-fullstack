package com.TheMFG.backend.model;

import com.TheMFG.backend.enums.Audience;
import com.TheMFG.backend.enums.ReplyRestriction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
@Table(name = "posts")
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(length = 256, nullable = false)
    private String content;

    @Column(name = "posted_date")
    private Date postedDate;

    @ManyToOne
    @JoinColumn(name = "author_id",nullable = false)
    private User author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_likes_junction",joinColumns = @JoinColumn(name = "post_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    @OneToMany
    private List<Image> images;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_reply_juntion",joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "reply_id"))
    @JsonIgnore
    private Set<Post> replies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_repost_juntion", joinColumns = @JoinColumn(name = "post_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> reposts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_bookmark_juntion", joinColumns = @JoinColumn(name = "post_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> bookmarks;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_view_juntion",joinColumns = @JoinColumn(name = "post_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> views;

    private boolean scheduled;

    @Column(name = "scheduled_date")
    private Date scheduldeDate;

    @Enumerated(EnumType.ORDINAL)
    private Audience audience;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "reply_restriction")
    private ReplyRestriction replyRestriction;

    public Post(){
        super();
        this.likes = new HashSet<>();
        this.images = new ArrayList<>();
        this.replies = new HashSet<>();
        this.reposts = new HashSet<>();
        this.bookmarks = new HashSet<>();
        this.views = new HashSet<>();
    }
}
