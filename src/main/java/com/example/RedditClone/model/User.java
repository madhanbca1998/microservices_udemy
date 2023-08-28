package com.example.RedditClone.model;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotEmpty(message = "UserName Cannot be Empty")
    private String username;
    @NotEmpty(message = "Password Cannot be Empty")
    private String password;
    @NotEmpty(message = "Email Cannot be Empty")
    @Email
     private String email;
    private boolean enabled;
    private Instant created;


}
