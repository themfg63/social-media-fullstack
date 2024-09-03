package com.TheMFG.backend.service;

import com.TheMFG.backend.dto.CreatePostDTO;
import com.TheMFG.backend.model.Image;
import com.TheMFG.backend.model.Post;
import com.TheMFG.backend.model.User;
import com.TheMFG.backend.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;

    public Post createPost(CreatePostDTO dto){
        Image savedGif;
        if(dto.getImages().size()>0){
            List<Image> gifList = dto.getImages();
            Image gif = gifList.get(0);
            gif.setImagePath(gif.getImageURL());

            savedGif = imageService.saveGifFromPost(gif);
            gifList.remove(0);
            gifList.add(savedGif);
            dto.setImages(gifList);
        }

        Post post = new Post();
        post.setContent(dto.getContent());
        if(dto.getScheduled()){
            post.setPostedDate(dto.getScheduledDate());
        }else{
            post.setPostedDate(new Date());
        }

        post.setAuthor(dto.getAuthor());
        post.setReplies(dto.getReplies());
        post.setScheduled(dto.getScheduled());
        post.setScheduldeDate(dto.getScheduledDate());
        post.setAudience(dto.getAudience());
        post.setReplyRestriction(dto.getReplyRestriction());
        post.setImages(dto.getImages());

        try{
            Post posted = postRepository.save(post);
            return posted;
        }catch (Exception e){
            throw new UnableToCreatePostException();
        }
    }

    public Post createMediaPost(String post, List<MultipartFile> files){
        CreatePostDTO dto = new CreatePostDTO();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            dto = objectMapper.readValue(post, CreatePostDTO.class);

            Post p = new Post();
            p.setContent(dto.getContent());
            if(dto.getScheduled()){
                p.setPostedDate(dto.getScheduledDate());
            }else{
                p.setPostedDate(new Date());
            }

            p.setAuthor(dto.getAuthor());
            p.setReplies(dto.getReplies());
            p.setScheduled(dto.getScheduled());
            p.setScheduldeDate(dto.getScheduledDate());
            p.setAudience(dto.getAudience());
            p.setReplyRestriction(dto.getReplyRestriction());

            List<Image> postImages = new ArrayList<>();

            for(int i = 0; i < files.size(); i++){
                Image postImage = imageService.uploadImage(files.get(i),"post");
                postImages.add(postImage);
            }

            p.setImages(postImages);

            return postRepository.save(p);
        }catch (Exception e){
            throw new UnableToCreatePostException();
        }
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post getPostById(Integer id){
        return postRepository.findById(id).orElseThrow(PostDoesNotExistException::new);
    }

    public Set<Post> getAllPostsByAuthor(User author){
        Set<Post> posts = postRepository.findByAuthor(author).orElse(new HashSet<>());
        return posts;
    }

    public void deletePost(Post post){
        postRepository.delete(post);
    }
}
