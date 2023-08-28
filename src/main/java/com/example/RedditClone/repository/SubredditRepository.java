package com.example.RedditClone.repository;

import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit,Long> {
    Subreddit findByName(String subredditName);
}