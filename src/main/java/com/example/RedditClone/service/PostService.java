package com.example.RedditClone.service;

import com.example.RedditClone.dto.PostRequest;
import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.exception.SpringRedditException;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.model.User;
import com.example.RedditClone.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SubredditRepository subredditRepository;
    @Autowired
    private AuthService authService;

    @Autowired
    private VoteRepository voteRepository;
@Autowired
private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    public Post createPost(PostRequest postRequest) {
        Post post=new Post();
     Subreddit subreddit= subredditRepository.findByName(postRequest.getSubredditName());
        System.out.println(subreddit);
    User currentUser= authService.getCurrentUser();
    post.setPostName(postRequest.getPostName());
    post.setUrl(postRequest.getUrl());
    post.setDescription(postRequest.getDescription());
    post.setCreatedDate(Instant.now());
    post.setUser(currentUser);
    post.setSubreddit(subreddit);
    postRepository.save(post);
    return post;
    }

    public List<PostResponse> getPostsByUsername(String username) {
      User user=  userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("No such user found with name: "+ username));
     return postRepository.findByUser(user)
              .stream()
              .map(this::mapToPostResponse)
              .collect(Collectors.toList());

    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
      Subreddit subreddit=  subredditRepository.findById(id).orElseThrow(()->new SpringRedditException("No subreddit found with id: "+ id));
     return postRepository.findAllBySubreddit(subreddit)
              .stream()
             .map(this::mapToPostResponse)
             .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
      Optional<Post> postOptional=  postRepository.findById(id);
     Post post= postOptional.orElseThrow(()->new SpringRedditException("No Post found with id:" + id));
      return PostResponse.builder()
              .id(post.getPostId())
              .postName(post.getPostName())
              .url(post.getUrl())
              .description(post.getDescription())
              .userName(post.getUser().getUsername())
              .subredditName(post.getSubreddit().getName()== null ? "No Subreddit" :post.getSubreddit().getName() )
              .commentCount(commentRepository.findByPost(post).size())
              .voteCount(voteRepository.findByPost(post).size())
              .duration(Date.from(postOptional.get().getCreatedDate()))
              .build();
    }

    public List<PostResponse> getAllPosts() {
      return  postRepository.findAll()
                .stream()
                .map(this::mapToPostResponse)
              .collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post post) {
     Post onePost=  postRepository.findById(post.getPostId()).orElseThrow(()->new SpringRedditException("No Such posts found for id : "+post.getPostId()));

        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName()== null ? "No Subreddit" :post.getSubreddit().getName() )
                .commentCount(commentRepository.findByPost(post).size())
                .voteCount(voteRepository.findByPost(post).size())
                .duration(Date.from(onePost.getCreatedDate()))
                .build();

    }
}
