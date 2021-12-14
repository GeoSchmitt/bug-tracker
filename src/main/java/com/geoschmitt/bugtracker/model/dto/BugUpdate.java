package com.geoschmitt.bugtracker.model.dto;

import com.geoschmitt.bugtracker.exceptions.BugTrackerException;
import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.Epic;
import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.model.enuns.EnumPriority;
import com.geoschmitt.bugtracker.model.enuns.EnumStatus;
import com.geoschmitt.bugtracker.repository.BugRepository;
import com.geoschmitt.bugtracker.repository.EpicRepository;
import com.geoschmitt.bugtracker.repository.UserRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Optional;

public class BugUpdate {

    @NotNull
    private Long id;

    @Size(min = 5)
    private String title;

    @Size(min = 10)
    private String description;

    private Long epic;

    private Long assignee;

    private EnumPriority priority;

    private EnumStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEpic() {
        return epic;
    }

    public void setEpic(Long epic) {
        this.epic = epic;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public EnumPriority getPriority() {
        return priority;
    }

    public void setPriority(EnumPriority priority) {
        this.priority = priority;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    public Bug update(BugRepository bugRepository, EpicRepository epicRepository, UserRepository userRepository) {
        if (this.status == EnumStatus.BACKLOG)
            throw new BugTrackerException("You cannot set the Bug to the backlog again");

        Optional<Bug> optionalBug = bugRepository.findById(this.id);
        if(!optionalBug.isPresent())
            throw new BugTrackerException("Invalid bug id");

        Bug bug = optionalBug.get();

        if(this.assignee != null)
            bug.setAssignee(getUser(getAssignee(),userRepository));

        if(this.title != null)
            bug.setTitle(this.title);

        if(this.description != null)
            bug.setDescription(this.description);

        if(this.epic != null)
            bug.setEpic(getEpic(getEpic(), epicRepository));

        if(this.priority != null)
            bug.setPriority(this.priority);

        if(this.status != null) {
            if (bug.getStatus() == EnumStatus.BACKLOG){
                bug.setStartedAt(Instant.now());
            }
            if(this.status == EnumStatus.DONE){
                bug.setDoneAt(Instant.now());
            }
            bug.setStatus(this.status);
        }

        bug.setUpdatedAt(Instant.now());
        
        return bug;
    }

    public Epic getEpic(Long id, EpicRepository epicRepository){
        if (id != null)
            return epicRepository.findById(id).get();
        return null;
    }

    public User getUser(Long id, UserRepository userRepository){
        if (id != null)
            return userRepository.findById(id).get();
        return null;
    }

}
