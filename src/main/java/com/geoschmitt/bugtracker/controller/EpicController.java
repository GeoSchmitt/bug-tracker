package com.geoschmitt.bugtracker.controller;

import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.Epic;
import com.geoschmitt.bugtracker.model.dto.*;
import com.geoschmitt.bugtracker.service.BugService;
import com.geoschmitt.bugtracker.service.EpicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/epic")
public class EpicController {

    @Autowired
    EpicService epicService;

    @PostMapping
    @CacheEvict(value="listEpics", allEntries = true)
    public ResponseEntity<EpicDto> createEpic(@RequestBody @Valid EpicDto epicDto, UriComponentsBuilder uriComponentsBuilder, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Epic epic = this.epicService.create(epicDto.convert(), token);
        URI uri = uriComponentsBuilder.path("/epic/{id}").buildAndExpand(epic.getId()).toUri();
        return ResponseEntity.created(uri).body(new EpicDto(epic));
    }

    @GetMapping
    @Cacheable(value = "listEpics")
    public ResponseEntity<Page<EpicDto>> listEpics(@RequestParam(required = false) String name,
                                 @PageableDefault Pageable pageable){
        Page<Epic> epics = this.epicService.list(pageable);
        return ResponseEntity.ok(EpicDto.convert(epics));
    }

    @PutMapping
    @CacheEvict(value="listEpics", allEntries = true)
    public ResponseEntity<EpicDto> updateEpic(@RequestBody @Valid EpicUpdate epicUpdate){
        Epic epic = this.epicService.update(epicUpdate);
        return ResponseEntity.ok(new EpicDto(epic));
    }

    @DeleteMapping("/{Id}")
    @CacheEvict(value="listEpics", allEntries = true)
    public ResponseEntity<String> deleteEpic(@PathVariable Long Id){
        this.epicService.delete(Id);
        return ResponseEntity.ok("Bug "+ Id +" deleted");
    }
}
