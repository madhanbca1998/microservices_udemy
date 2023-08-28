package com.example.RedditClone.controller;


import com.example.RedditClone.dto.VoteDto;
import com.example.RedditClone.model.VoteType;
import com.example.RedditClone.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/vote/")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<String> vote(@RequestBody VoteDto voteDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(voteService.vote(voteDto));
    }

}
