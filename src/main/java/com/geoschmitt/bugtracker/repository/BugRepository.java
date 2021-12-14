package com.geoschmitt.bugtracker.repository;

import com.geoschmitt.bugtracker.model.Bug;
import com.geoschmitt.bugtracker.model.User;
import com.geoschmitt.bugtracker.model.enuns.EnumStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Long> {

    Page<Bug> findByTitle(String title, Pageable pageable);
    Page<Bug> findByStatus(EnumStatus status, Pageable pageable);
    Page<Bug> findByReporter(User user, Pageable pageable);
    Page<Bug> findByReporterAndStatus(User user, EnumStatus status, Pageable pageable);
    Page<Bug> findByAssignee(User user, Pageable pageable);
    Page<Bug> findByAssigneeAndStatus(User user, EnumStatus status, Pageable pageable);

}
