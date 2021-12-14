package com.geoschmitt.bugtracker.service;

import com.geoschmitt.bugtracker.config.security.TokenService;
import com.geoschmitt.bugtracker.model.Epic;
import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.model.dto.EpicUpdate;
import com.geoschmitt.bugtracker.repository.EpicRepository;
import com.geoschmitt.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EpicService {

    @Autowired
    EpicRepository epicRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenService tokenService;


    public Epic create(Epic epic, String token) {
        User user = this.tokenService.getUser(token, this.userRepository);
        epic.setReporter(user);
        this.epicRepository.save(epic);
        return epic;
    }

    public Page<Epic> list(Pageable pageable){
        Page<Epic> epics = this.epicRepository.findAll(pageable);
        return epics;
    }

    public Epic update(EpicUpdate epicUpdate) {
        Epic epic = epicUpdate.update(this.epicRepository);
        this.epicRepository.save(epic);
        return epic;
    }

    public void delete(Long id) {
        Optional<Epic> bug = this.epicRepository.findById(id);
        if(bug.isPresent())
            this.epicRepository.delete(bug.get());
    }
}
