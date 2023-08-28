package com.example.RedditClone.controller;


import com.example.RedditClone.dto.PostRequest;
import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(postRequest));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllPosts());

    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPostById(id));

    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPostsBySubreddit(id));

    }
    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPostsByUsername(username));

    }

}
