package com.TheMFG.backend.controller;

import com.TheMFG.backend.exception.UnableToSavePhotoException;
import com.TheMFG.backend.model.User;
import com.TheMFG.backend.service.ImageService;
import com.TheMFG.backend.service.TokenService;
import com.TheMFG.backend.service.UserService;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final ImageService imageService;

    @GetMapping("/verify")
    public User verifyIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
        String username = tokenService.getUsernameFromToken(token);
        return userService.getUserByUsername(username);
    }

    @PostMapping("/pfp")
    public User uploadProfilePhoto(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,
                                   @RequestParam("image")MultipartFile file) throws UnableToSavePhotoException{
        String username = tokenService.getUsernameFromToken(token);
        return userService.setProfileOrBannerPicture(username,file,"pfp");
    }

    @PostMapping("/")
    public User updateUser(@RequestBody User user){
        return userService.update(user);
    }

    @PutMapping("follow")
    public Set<User> followUsers(@RequestHeader(HttpHeaders.AUTHORIZATION)String token, @RequestBody Map<String,String> body){
        String loggedInUser = tokenService.getUsernameFromToken(token);
        String followeduser = body.get("followedUser");
        return userService.followUser(loggedInUser,followeduser);
    }

    @GetMapping("/following/{username}")
    public Set<User> getFollowingList(@PathVariable String username){
        return userService.retrieveFollowingList(username);
    }

    @GetMapping("/followers/{username}")
    public Set<User> getFollowersList(@PathVariable String username){
        return userService.retrieveFollowersList(username);
    }
}
