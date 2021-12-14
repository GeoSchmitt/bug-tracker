package com.geoschmitt.bugtracker.service;

import com.geoschmitt.bugtracker.config.security.TokenService;
import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.Epic;
import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.model.dto.BugForm;
import com.geoschmitt.bugtracker.model.dto.BugUpdate;
import com.geoschmitt.bugtracker.model.enuns.EnumPriority;
import com.geoschmitt.bugtracker.model.enuns.EnumStatus;
import com.geoschmitt.bugtracker.repository.BugRepository;
import com.geoschmitt.bugtracker.repository.EpicRepository;
import com.geoschmitt.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BugService {

    @Autowired
    BugRepository bugRepository;
    @Autowired
    EpicRepository epicRepository;
    @Autowired
    UserRepository userRepository;

    public Bug create(BugForm bugForm, User user){
        Bug bug = bugForm.convert(this.epicRepository);
        bug.setReporter(user);
        this.bugRepository.save(bug);
        return bug;
    }

    public Page<Bug> list(String title, Pageable pageable){

        Page<Bug> bugs;
        if (title != null)
            bugs = this.bugRepository.findByTitle(title, pageable);
        else
            bugs = this.bugRepository.findAll(pageable);

        return bugs;
    }

    public void delete(Long bugId) {
        Optional<Bug> bug = this.bugRepository.findById(bugId);
        if(bug.isPresent())
            this.bugRepository.delete(bug.get());
    }

    public Bug update(BugUpdate bugUpdate) {
        Bug bug = bugUpdate.update(this.bugRepository, this.epicRepository, this.userRepository);
        this.bugRepository.save(bug);
        return bug;
    }

    public Bug detail(Long id) {
        Bug bug = this.bugRepository.findById(id).get();
        return bug;
    }

    public Page<Bug> listByStatus(EnumStatus status, Pageable pageable) {
        Page<Bug> bugs = this.bugRepository.findByStatus(status, pageable);
        return bugs;
    }

    public Page<Bug> listByReporter(User user, EnumStatus status, Pageable pageable) {
        if(status != null)
            return this.bugRepository.findByReporter(user, pageable);
        return this.bugRepository.findByReporterAndStatus(user, status, pageable);
    }

    public Page<Bug> listByAssignee(User user, EnumStatus status, Pageable pageable) {
        if (status != null)
            return this.bugRepository.findByAssigneeAndStatus(user, status, pageable);
       return this.bugRepository.findByAssignee(user, pageable);
    }

    public List<Bug> listByFilter(EnumStatus status, Long assignee, Long reporter, EnumPriority priority, Long epic) {
        List<Bug> bugs = this.bugRepository.findAll();
        bugs = bugs.stream().filter(b ->
                (status != null ? b.getStatus().name().equals(status.name()) : true)
                && (assignee != null ? b.getAssignee().getId() == assignee : true)
                && (reporter != null ? b.getReporter().getId() == reporter : true)
                && (priority != null ? b.getPriority().name().equals(priority.name()) : true)
                && (epic != null ? b.getEpic().getId() == epic : true)).collect(Collectors.toList());
        return bugs;

    }
}
