package com.example.RedditClone.service;


import com.example.RedditClone.dto.VoteDto;
import com.example.RedditClone.exception.SpringRedditException;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Vote;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.RedditClone.model.VoteType.UPVOTE;

@Service
@Transactional
public class VoteService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public String vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        }
        else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
        return "Vote has been done";
    }
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
