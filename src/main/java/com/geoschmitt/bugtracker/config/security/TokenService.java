package com.geoschmitt.bugtracker.config.security;

import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${bugtracker.jwt.expiration}")
    private String expiration;

    @Value("${bugtracker.jwt.secret}")
    private String secret;

    public String tokenGenerate(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Date now = new Date();
        return Jwts.builder()
                .setIssuer("REST Bug Tracker")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Long.parseLong(this.expiration)))
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    public Boolean isValidToken(String token){
        try{
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public Long getIdUser(String token){
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public User getUser(String token, UserRepository userRepository) {
        Long userId = this.getIdUser(token.substring(7, token.length()));
        return userRepository.findById(userId).get();
    }
}
