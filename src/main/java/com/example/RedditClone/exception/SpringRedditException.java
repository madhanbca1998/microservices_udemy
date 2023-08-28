package com.example.RedditClone.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SpringRedditException extends RuntimeException{

    public SpringRedditException(String message){
        super(message);

    }

}
