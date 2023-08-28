package com.example.RedditClone.service;


import com.example.RedditClone.dto.AuthenticationResponse;
import com.example.RedditClone.dto.LoginRequest;
import com.example.RedditClone.dto.RefreshTokenRequest;
import com.example.RedditClone.dto.RegisterRequest;
import com.example.RedditClone.exception.SpringRedditException;
import com.example.RedditClone.model.NotificationEmail;
import com.example.RedditClone.model.User;
import com.example.RedditClone.model.VerificationToken;
import com.example.RedditClone.repository.UserRepository;
import com.example.RedditClone.repository.VerificationRepository;
import com.example.RedditClone.security.JwtProvider;
import org.springframework.security.oauth2.jwt.Jwt;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  VerificationRepository verificationRepository;
    @Autowired
    private  MailService mailService;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtProvider jwtProvider;
    @Autowired
    private  UserDetailsServiceImpl userDetailsService;
@Autowired
private RefreshTokenService refreshTokenService;
//    Register Implement
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user=new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

       String token= generateVerificationToken(user);

//       mailService.sendMail(new NotificationEmail("Please Activate Your Account" ,               user.getEmail() ,
//               "Thanks your for signing into Spring Reddit" + "Please click on the below url to activate your account :  "+
//               "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generateVerificationToken(User user){
       String token= UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public void verifyAccount(String token) {
      Optional<VerificationToken> verificationToken= verificationRepository.findByToken(token);
      verificationToken.orElseThrow(()->new SpringRedditException("Invalid Token"));
      fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
       String username= verificationToken.getUser().getUsername();
     User user=  userRepository.findByUsername(username).
     orElseThrow(()->new SpringRedditException("User not Found"));
     user.setEnabled(true);
     userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        String token=null;
     Authentication authentication=   authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
     if(authentication.isAuthenticated()){
       token =  jwtProvider.generateToken(loginRequest.getUsername());
     }
     else{
         System.out.println("Invalid User");
         throw new SpringRedditException("Invalid user");
     }
     return AuthenticationResponse.builder()
             .authenticationToken(token)
             .refreshToken(refreshTokenService.generateRefreshToken().getToken())
             .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
             .username(loginRequest.getUsername())
             .build();

    }


    @Transactional
    public User getCurrentUser() {
        String subject = SecurityContextHolder.
                getContext().getAuthentication().getName();
        return userRepository.findByUsername(subject)
                .orElseThrow(() -> new SpringRedditException("User name not found - "));
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
       String token= jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
       return AuthenticationResponse.builder()
               .authenticationToken(token)
               .refreshToken(refreshTokenRequest.getRefreshToken())
               .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
               .username(refreshTokenRequest.getUsername())
               .build();
    }


}
