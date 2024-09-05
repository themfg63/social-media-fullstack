package com.TheMFG.backend.controller;

import com.TheMFG.backend.dto.CreatePostDTO;
import com.TheMFG.backend.exception.PostDoesNotExistException;
import com.TheMFG.backend.exception.UnableToCreatePostException;
import com.TheMFG.backend.model.Post;
import com.TheMFG.backend.model.User;
import com.TheMFG.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ExceptionHandler({UnableToCreatePostException.class})
    public ResponseEntity<String> handleUnableToCreatePost(){
        return new ResponseEntity<>("Şu anda Gönderi Oluşturulamıyor",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({PostDoesNotExistException.class})
    public ResponseEntity<String> handlePostDoesntExist(){
        return new ResponseEntity<>("Post Bulunamadı", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @PostMapping
    public Post createPost(@RequestBody CreatePostDTO postDTO){
        return postService.createPost(postDTO);
    }

    @PostMapping(value = "/media",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public Post createMediaPost(@RequestPart("post") String post, @RequestPart("media")List<MultipartFile> files){
        return postService.createMediaPost(post,files);
    }

    @GetMapping("/id/{id}")
    public Post getPostById(@PathVariable int id){
        return postService.getPostById(id);
    }

    @GetMapping("/author/{userId}")
    public Set<Post> getAllPostsByAuthor(@PathVariable("userId")int userId){
        User author = new User();
        author.setUserId(userId);
        return postService.getAllPostsByAuthor(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id){
        postService.deletePost
    }
}
