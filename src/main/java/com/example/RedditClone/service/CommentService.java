package com.example.RedditClone.service;

import com.example.RedditClone.dto.CommentDto;
import com.example.RedditClone.exception.SpringRedditException;
import com.example.RedditClone.model.Comment;
import com.example.RedditClone.model.NotificationEmail;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.User;
import com.example.RedditClone.repository.CommentRepository;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private MailContentBuilder mailContentBuilder;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;
    public String createComment(CommentDto commentDto){
      Post post= postRepository.findById(commentDto.getPostId())
                .orElseThrow(()->new SpringRedditException("No Posts Found with id: "+ commentDto.getPostId()));

      Comment comment= Comment.builder()
              .text(commentDto.getText())
              .post(post)
              .user(authService.getCurrentUser())
              .createdDate(Instant.now())
              .build();
      commentRepository.save(comment);
     String message= mailContentBuilder.build(post.getUser().getUsername()+ " posted a comment on your post."+ post.getUrl());
     mailSendCommentNotification(message,post.getUser());
      return "Commented Successfully";


    }

    private void mailSendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(
                user.getUsername() + "commented on your post",
                user.getEmail(),
                message
        ));
        System.out.println("Comment Notification Email sent");
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
     Post post= postRepository.findById(postId).orElseThrow(()->new SpringRedditException("No posts found for id:" + postId));
    return commentRepository.findByPost(post)
             .stream()
             .map(this::mapToCommentDto)
             .collect(Collectors.toList());
    }

    private CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getPostId())
                .createdDate(comment.getCreatedDate())
                .text(comment.getText())
                .userName(comment.getUser().getUsername())
                .build();
    }

    public List<CommentDto> getAllCommentsForUser(String username) {
      User user=  userRepository.findByUsername(username)
                .orElseThrow(()->new SpringRedditException("No user found for :"+ username));
      return commentRepository.findByUser(user)
              .stream()
              .map(this::mapToCommentDto)
              .collect(Collectors.toList());
    }
}
