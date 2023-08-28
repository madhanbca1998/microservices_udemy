package com.example.RedditClone.controller;


import com.example.RedditClone.dto.CommentDto;
import com.example.RedditClone.model.Comment;
import com.example.RedditClone.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(commentDto));
    }

    @GetMapping("/by-post/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(id));
    }

    @GetMapping("/by-user/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForUser(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForUser(username));
    }

}
