package com.geoschmitt.bugtracker.service;

import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenService tokenService;

    public User getUser(Long userId, String token){
        if(userId != null)
            return this.userRepository.findById(userId).get();
        return this.tokenService.getUser(token, this.userRepository);
    }
}
