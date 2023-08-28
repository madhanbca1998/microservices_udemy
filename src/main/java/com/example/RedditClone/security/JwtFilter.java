package com.example.RedditClone.security;


import com.example.RedditClone.exception.SpringRedditException;
import com.example.RedditClone.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
private JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String authHeader=request.getHeader("Authorization");
            String token=null;
            String username=null;
            if(authHeader !=null && authHeader.startsWith("Bearer ")){
                token=authHeader.substring(7);
                username= jwtProvider.getUsernameFromToken(token);

            }
            if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= userDetailsServiceImpl.loadUserByUsername(username);

                if(jwtProvider.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authToken=
                            new  UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
            filterChain.doFilter(request,response);
        }
        catch(Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported JWT token");

        }

    }


}