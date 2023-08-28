package com.example.RedditClone.service;


import com.example.RedditClone.dto.SubredditDto;
import com.example.RedditClone.exception.SpringRedditException;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.model.User;
import com.example.RedditClone.repository.SubredditRepository;
import com.example.RedditClone.repository.UserRepository;
import com.example.RedditClone.security.JwtFilter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class SubredditService {

    private final SubredditRepository subredditRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    public SubredditDto save(SubredditDto subredditDto){
      Subreddit subreddit=subredditRepository.save(mapSubredditDto(subredditDto));
      subredditDto.setId(subreddit.getId());
      return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
       return Subreddit.builder()
                .name(subredditDto.getSubredditName())
                .description(subredditDto.getDescription())
                .createdDate(Instant.now())
                .user(authService.getCurrentUser())
                .build();
    }


    public List<SubredditDto> getAllSubreddits() {
       return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder()
                .id(subreddit.getId())
                .subredditName(subreddit.getName())
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    public SubredditDto getSubredditById(Long id) {
        SubredditDto subredditDto=new SubredditDto();
      Subreddit subreddit=  subredditRepository.findById(id).orElseThrow(()->new SpringRedditException("Subreddit with " + id + " not found"));
      subredditDto.setId(subreddit.getId());
      subredditDto.setSubredditName(subreddit.getName());
      subredditDto.setDescription(subreddit.getDescription());
      subredditDto.setNumberOfPosts(subreddit.getPosts().size());
      return subredditDto;


    }
}
