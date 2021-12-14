package com.geoschmitt.bugtracker.model.dto;

import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.Epic;
import com.geoschmitt.bugtracker.repository.BugRepository;
import com.geoschmitt.bugtracker.repository.EpicRepository;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

public class EpicUpdate {

    @NotNull
    private Long id;

    @Size(min = 5)
    private String name;

    @Size(min = 5)
    private String description;

    public EpicUpdate(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Epic update(EpicRepository epicRepository) {
        Epic epic = epicRepository.findById(this.id).get();

        if(this.name != null)
            epic.setName(this.name);

        if(this.description != null)
            epic.setDescription(this.description);

        epic.setUpdatedAt(Instant.now());

        return epic;
    }


}
