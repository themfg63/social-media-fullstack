package com.TheMFG.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Name;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false)
    private Integer userId;

    private String firstName;
    private String lastName;
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(name = "dt")
    private Date dateOfBirth;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String bio;
    private String nickname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_photo",referencedColumnName = "image_id")
    private Image profilePhoto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "banner_photo", referencedColumnName = "image_id")
    private Image bannerPhoto;

    private Boolean enabled;

    @JsonIgnore
    private Long verification;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "following", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    @JsonIgnore
    private Set<User> following;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "followers", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "follower_id"))
    @JsonIgnore
    private Set<User> followers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles_junction",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> authorities;

    public User(){
        this.authorities = new HashSet<>();
        this.following = new HashSet<>();
        this.followers = new HashSet<>();
        this.enabled = false;
    }
}
