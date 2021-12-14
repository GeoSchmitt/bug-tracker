package com.geoschmitt.bugtracker.model.dto;

import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.Epic;
import com.geoschmitt.bugtracker.model.enuns.EnumPriority;
import com.geoschmitt.bugtracker.repository.EpicRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BugForm {

    private Long id;

    @NotNull @NotEmpty @Size(min = 5)
    private String title;

    @NotNull @NotEmpty @Size(min = 10)
    private String description;

    private Long epic;

    private EnumPriority priority = EnumPriority.NO_PRIORITY;

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

    public EnumPriority getPriority() {
        return priority;
    }

    public void setPriority(EnumPriority priority) {
        this.priority = priority;
    }

    public Bug convert(EpicRepository epicRepository){
        return new Bug(this.title, this.description, getEpic(this.epic, epicRepository), this.priority);
    }

    public Epic getEpic(Long id, EpicRepository epicRepository){
        if (id != null)
            return epicRepository.findById(id).get();
        return null;
    }
}
