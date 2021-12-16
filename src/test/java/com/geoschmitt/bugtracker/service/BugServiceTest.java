package com.geoschmitt.bugtracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.model.dto.BugForm;
import com.geoschmitt.bugtracker.model.dto.BugUpdate;
import com.geoschmitt.bugtracker.model.enuns.EnumPriority;
import com.geoschmitt.bugtracker.model.enuns.EnumStatus;
import com.geoschmitt.bugtracker.repository.BugRepository;
import com.geoschmitt.bugtracker.repository.EpicRepository;
import com.geoschmitt.bugtracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BugServiceTest {

    @Autowired
    private BugService bugService;
    @MockBean
    private BugRepository bugRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EpicRepository epicRepository;


    public Bug bugFactory(EnumStatus status, EnumPriority priority){
        Bug bug = new Bug();
        bug.setStatus(status);
        bug.setPriority(priority);
        return bug;
    }

    @Test
    public void shouldBeSuccessfull_create(){
        BugForm bugForm = new BugForm();
        bugForm.setTitle("Test Title");
        bugForm.setDescription("Test Description");
        User user = new User();
        user.setId(1L);
        user.setEmail("teste@email.com");
        user.setPassword("1234");
        Bug bug = bugForm.convert(this.epicRepository);
        Mockito.when(this.bugRepository.save(bug)).thenReturn(null);
        Bug result = this.bugService.create(bugForm, user);
        assertThat(result.getTitle()).isEqualTo(bug.getTitle());
    }

    @Test
    public void shouldBeSuccessfull_list(){
        Pageable pageable = null;
        Mockito.when(this.bugRepository.findAll(pageable)).thenReturn(null);
        Page<Bug> result = this.bugService.list(null, pageable);
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void shouldBeSuccessfull_delete(){
        Bug bug = new Bug();
        bug.setId(1L);
        bug.setTitle("Test Bug title");
        Mockito.when(this.bugRepository.findById(1L)).thenReturn(Optional.of(bug));
        Boolean result = this.bugService.delete(1L);
        assertThat(result).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void shouldBeSuccessfull_update_title(){
        BugUpdate bugUpdate = new BugUpdate();
        bugUpdate.setTitle("New title");
        bugUpdate.setId(1L);
        Bug bug = new Bug();
        bug.setTitle("title");
        bug.setId(1L);

        Mockito.when(this.bugRepository.findById(1L)).thenReturn(Optional.of(bug));
        bug.setTitle(bugUpdate.getTitle());
        Mockito.when(this.bugRepository.save(bug)).thenReturn(null);
        Bug result = this.bugService.update(bugUpdate);
        assertThat(result.getTitle() == bugUpdate.getTitle());
    }

    @Test
    public void shouldBeSuccessfull_update_statusToInProgress(){
        BugUpdate bugUpdate = new BugUpdate();
        bugUpdate.setId(1L);
        bugUpdate.setStatus(EnumStatus.IN_PROGRESS);

        Bug bug = new Bug();
        bug.setId(1L);
        bug.setStatus(EnumStatus.BACKLOG);

        Mockito.when(this.bugRepository.findById(1L)).thenReturn(Optional.of(bug));
        Mockito.when(this.bugRepository.save(bug)).thenReturn(null);
        Bug result = this.bugService.update(bugUpdate);

        assertThat(result.getStatus()).isEqualTo(EnumStatus.IN_PROGRESS);
        assertThat(result.getStartedAt()).isNotNull();
        assertThat(result.getDoneAt()).isNull();
    }

    @Test
    public void shouldBeSuccessfull_update_statusToDone(){
        BugUpdate bugUpdate = new BugUpdate();
        bugUpdate.setId(1L);
        bugUpdate.setStatus(EnumStatus.DONE);

        Bug bug = new Bug();
        bug.setId(1L);
        bug.setStatus(EnumStatus.REVIEW);

        Mockito.when(this.bugRepository.findById(1L)).thenReturn(Optional.of(bug));
        Mockito.when(this.bugRepository.save(bug)).thenReturn(null);
        Bug result = this.bugService.update(bugUpdate);

        assertThat(result.getStatus()).isEqualTo(EnumStatus.DONE);
        assertThat(result.getDoneAt()).isNotNull();
    }

    @Test
    public void shouldBeSuccessfull_listByfilter(){

        List<Bug> mockList = new ArrayList<>();
        mockList.add(bugFactory(EnumStatus.BACKLOG, EnumPriority.HIGH));
        mockList.add(bugFactory(EnumStatus.BACKLOG, EnumPriority.LOW));
        mockList.add(bugFactory(EnumStatus.BACKLOG, EnumPriority.LOW));
        mockList.add(bugFactory(EnumStatus.IN_PROGRESS, EnumPriority.LOW));

        Mockito.when(this.bugRepository.findAll()).thenReturn(mockList);
        List<Bug> result = this.bugService.listByFilter(EnumStatus.BACKLOG, null, null, EnumPriority.LOW, null);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getStatus()).isEqualTo(EnumStatus.BACKLOG);
        assertThat(result.get(0).getPriority()).isEqualTo(EnumPriority.LOW);
        assertThat(result.get(1).getStatus()).isEqualTo(EnumStatus.BACKLOG);
        assertThat(result.get(1).getPriority()).isEqualTo(EnumPriority.LOW);
    }



}
