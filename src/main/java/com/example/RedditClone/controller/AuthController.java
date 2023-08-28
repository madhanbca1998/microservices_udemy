package com.example.RedditClone.controller;


import com.example.RedditClone.dto.AuthenticationResponse;
import com.example.RedditClone.dto.LoginRequest;
import com.example.RedditClone.dto.RefreshTokenRequest;
import com.example.RedditClone.dto.RegisterRequest;
import com.example.RedditClone.security.JwtProvider;
import com.example.RedditClone.service.AuthService;
import com.example.RedditClone.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;



   @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
       authService.signup(registerRequest);
       return new ResponseEntity<>("User Registration Successfully", HttpStatus.OK);

   }
   @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
       authService.verifyAccount(token);
       return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);

   }

   @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody LoginRequest loginRequest){
      return ResponseEntity.status(HttpStatus.OK)
              .body(authService.login(loginRequest));

   }
    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshToken (@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.refreshToken(refreshTokenRequest));

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Refresh Token Deleted Successfully");

    }


}
