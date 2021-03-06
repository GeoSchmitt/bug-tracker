package com.geoschmitt.bugtracker.controller;

import com.geoschmitt.bugtracker.config.exceptions.BugTrackerException;
import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.model.dto.BugDto;
import com.geoschmitt.bugtracker.model.dto.BugForm;
import com.geoschmitt.bugtracker.model.dto.BugUpdate;
import com.geoschmitt.bugtracker.model.enuns.EnumPriority;
import com.geoschmitt.bugtracker.model.enuns.EnumStatus;
import com.geoschmitt.bugtracker.service.BugService;
import com.geoschmitt.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bug")
public class BugController {

    @Autowired
    BugService bugService;
    @Autowired
    UserService userService;

    /**
     * Endpoint to create the Bug
     *
     * @param bugForm {@link BugForm}
     * @return {@link ResponseEntity<BugDto>}
     */
    @PostMapping
    public ResponseEntity<BugDto> createBug(@RequestBody @Valid BugForm bugForm, UriComponentsBuilder uriComponentsBuilder, HttpServletRequest request){
        User user = userService.getUser(null, request.getHeader("Authorization"));
        Bug bug = this.bugService.create(bugForm, user);
        URI uri = uriComponentsBuilder.path("/bug/{id}").buildAndExpand(bug.getId()).toUri();
        return ResponseEntity.created(uri).body(new BugDto(bug));
    }

    /**
     * Endpoint to detail a Bug
     *
     * @param  id from {@link Bug}
     * @return {@link ResponseEntity<BugDto>}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BugDto> detailBug(@PathVariable Long id){
        Bug bug = this.bugService.detail(id);
        return  ResponseEntity.ok(new BugDto(bug));
    }

    /**
     * Endpoint to list Bugs
     *
     * @param title from {@link Bug}
     * @return {@link Page} {@link BugDto}
     */
    @GetMapping
    public ResponseEntity<Page<BugDto>> listBugs(@RequestParam(required = false) String title,
                                 @PageableDefault Pageable pageable){
        Page<Bug> bugs = this.bugService.list(title, pageable);
        return ResponseEntity.ok(BugDto.convert(bugs));
    }

    /**
     * Endpoint to list Bugs by Status
     *
     * @param status as {@link EnumStatus} from {@link Bug}
     * @return {@link Page} {@link BugDto}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<BugDto>> listBugsByStatus(@PathVariable EnumStatus status,
                                                 @PageableDefault Pageable pageable){
        Page<Bug> bugs = this.bugService.listByStatus(status, pageable);
        return ResponseEntity.ok(BugDto.convert(bugs));
    }

    /**
     * Endpoint to list Bugs by the assignee
     *
     * @param status as {@link EnumStatus} from {@link Bug}
     * @param assignee id from {@link User} in the {@link Bug}
     * @return {@link Page} {@link BugDto}
     */
    @GetMapping("/assignee")
    public ResponseEntity<Page<BugDto>> listByAssignee(@RequestParam(required = false) EnumStatus status,
                                                       @RequestParam(required = false) Long assignee,
                                                       @PageableDefault Pageable pageable,
                                                       HttpServletRequest request){

        User user = this.userService.getUser(assignee, request.getHeader("Authorization"));

        Page<Bug> bugs = this.bugService.listByAssignee(user, status,  pageable);
        return ResponseEntity.ok(BugDto.convert(bugs));
    }

    /**
     * Endpoint to list Bugs by the reporter
     *
     * @param status as {@link EnumStatus} from {@link Bug}
     * @param reporter id from {@link User} in the {@link Bug}
     * @return {@link Page} {@link BugDto}
     */
    @GetMapping("/reported")
    public ResponseEntity<Page<BugDto>> listByReporter(@RequestParam(required = false) EnumStatus status,
                                                       @RequestParam(required = false) Long reporter,
                                                       @PageableDefault Pageable pageable,
                                                       HttpServletRequest request){

        User user = this.userService.getUser(reporter, request.getHeader("Authorization"));
        Page<Bug> bugs = this.bugService.listByReporter(user, status, pageable);
        return ResponseEntity.ok(BugDto.convert(bugs));
    }


    /**
     * Endpoint to list Bugs by a custom filter
     *
     * @param status as {@link EnumStatus} from {@link Bug}
     * @param reporter id from {@link User} in the {@link Bug}
     * @param assignee id from {@link User} in the {@link Bug}
     * @param status as {@link EnumPriority} from {@link Bug}
     * @param epic id from {@link com.geoschmitt.bugtracker.model.Epic } from {@link Bug}
     * @return {@link Page} {@link BugDto}
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<BugDto>> listByFilter(@RequestParam(required = false) EnumStatus status,
                                                     @RequestParam(required = false) Long assignee,
                                                     @RequestParam(required = false) Long reporter,
                                                     @RequestParam(required = false) EnumPriority priority,
                                                     @RequestParam(required = false) Long epic,
                                                     @PageableDefault Pageable pageable ){

        List<Bug> bugs = this.bugService.listByFilter(status, assignee, reporter, priority, epic);
        List<BugDto> bugsDto = BugDto.convert(bugs);
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > bugsDto.size() ? bugsDto.size() : (start + pageable.getPageSize());
        Page<BugDto> page = new PageImpl<BugDto>(bugsDto.subList(start, end), pageable, bugsDto.size());

        return ResponseEntity.ok(page);
    }

    /**
     * Endpoint to update info in a Bug
     *
     * @param bugUpdate {@link BugUpdate}
     * @return {@link ResponseEntity<?>}
     */
    @PutMapping
    public ResponseEntity<?> updateBug(@RequestBody @Valid BugUpdate bugUpdate){
        try {
            Bug bug = this.bugService.update(bugUpdate);
            return ResponseEntity.ok(new BugDto(bug));
        }catch (BugTrackerException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Endpoint to delete Bug
     *
     * @param id from {@link Bug}
     * @return {@link ResponseEntity<String>}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBug(@PathVariable Long id){
        Boolean deleted = this.bugService.delete(id);
        if (deleted)
            return ResponseEntity.ok("Bug "+ id +" deleted");
        else
            return ResponseEntity.internalServerError().body("Some error happened");
    }

}
