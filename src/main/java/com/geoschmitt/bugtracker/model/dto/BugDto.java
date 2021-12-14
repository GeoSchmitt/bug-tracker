package com.geoschmitt.bugtracker.model.dto;

import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.Epic;
import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.model.enuns.EnumPriority;
import com.geoschmitt.bugtracker.model.enuns.EnumStatus;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class BugDto {
    private Long id;

    private UserDto assignee;

    private UserDto reporter;

    private String title;

    private String description;

    private EpicDto epic;

    private EnumPriority priority;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant startedAt;

    private Instant doneAt;

    private EnumStatus status;

    public BugDto(Bug bug) {
        setId(bug.getId());
        setAssignee(bug.getAssignee());
        setReporter(bug.getReporter());
        setTitle(bug.getTitle());
        setDescription(bug.getDescription());
        setEpic(bug.getEpic());
        setPriority(bug.getPriority());
        setCreatedAt(bug.getCreatedAt());
        setUpdatedAt(bug.getUpdatedAt());
        setStartedAt(bug.getStartedAt());
        setDoneAt(bug.getDoneAt());
        setStatus(bug.getStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee == null ? null : new UserDto(assignee);
    }

    public UserDto getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = new UserDto(reporter);
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

    public EpicDto getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic == null ? null : new EpicDto(epic);
    }

    public EnumPriority getPriority() {
        return priority;
    }

    public void setPriority(EnumPriority priority) {
        this.priority = priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(Instant doneAt) {
        this.doneAt = doneAt;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    public static Page<BugDto> convert(Page<Bug> bugs){
        return bugs.map(BugDto::new);
    }
    public static List<BugDto> convert(List<Bug> bugs){
        return bugs.stream().map(BugDto::new).collect(Collectors.toList());
    }
}
