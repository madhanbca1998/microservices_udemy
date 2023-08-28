package com.example.RedditClone.service;


import com.example.RedditClone.exception.SpringRedditException;
import com.example.RedditClone.model.RefreshToken;
import com.example.RedditClone.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new SpringRedditException("Invalid refresh token"));
    }

    public String deleteRefreshToken(String token){
      return  refreshTokenRepository.deleteByToken(token)
                .orElseThrow(()->new SpringRedditException("No refresh token found"));

    }
}
