package com.geoschmitt.bugtracker.model;

import com.geoschmitt.bugtracker.model.enuns.EnumPriority;
import com.geoschmitt.bugtracker.model.enuns.EnumStatus;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User assignee;

    @ManyToOne
    private User reporter;

    private String title;

    private String description;

    @ManyToOne
    private Epic epic;

    @Enumerated(EnumType.STRING)
    private EnumPriority priority = EnumPriority.NO_PRIORITY;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    private Instant startedAt;

    private Instant doneAt;

    @Enumerated(EnumType.STRING)
    private EnumStatus status = EnumStatus.BACKLOG;

    public Bug() {
    }

    public Bug(String title, String description, Epic epic, EnumPriority priority) {
        this.title = title;
        this.description = description;
        this.epic = epic;
        this.priority = priority;
    }

    public Bug(Long id, User reporter, User assignee, String title, String description, Epic epic, EnumPriority priority, Instant createdAt, Instant updatedAt, Instant startedAt, Instant doneAt, EnumStatus status) {
        this.id = id;
        this.reporter = reporter;
        this.assignee = assignee;
        this.title = title;
        this.description = description;
        this.epic = epic;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startedAt = startedAt;
        this.doneAt = doneAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
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

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
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

}
