package com.geoschmitt.bugtracker.config.security;

import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.repository.UserRepository;
import com.geoschmitt.bugtracker.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthenticationTokenFilter extends OncePerRequestFilter {


    private TokenService tokenService;
    private UserRepository userRepository;

    public AuthenticationTokenFilter(TokenService tokenService, UserRepository userRepository){
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = tokenRecover(request);
        if(tokenService.isValidToken(token))
            this.userAuthenticate(token);

        filterChain.doFilter(request, response);
    }

    public String tokenRecover(HttpServletRequest req){
        String token = req.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer "))
            return null;
        return token.substring(7, token.length());
    }

    private void userAuthenticate(String token){
        Optional<User> user = userRepository.findById(tokenService.getIdUser(token));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.get(), null, user.get().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
