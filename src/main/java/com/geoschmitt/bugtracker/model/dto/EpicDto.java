package com.geoschmitt.bugtracker.model.dto;

import com.geoschmitt.bugtracker.model.Epic;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EpicDto {
    private Long id;

    @NotNull @NotEmpty @Size(min = 5)
    private String name;

    @NotNull @NotEmpty @Size(min = 5)
    private String description;

    private UserDto reporter;

    public EpicDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public EpicDto(Epic epic) {
        setId(epic.getId());
        setName(epic.getName());
        setDescription(epic.getDescription());
        setReporter(new UserDto(epic.getReporter()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDto getReporter() {
        return reporter;
    }

    public void setReporter(UserDto reporter) {
        this.reporter = reporter;
    }

    public static Page<EpicDto> convert(Page<Epic> epics) {
        return epics.map(EpicDto::new);
    }

    public Epic convert(){
        return new Epic(this.name, this.description);
    }
}
