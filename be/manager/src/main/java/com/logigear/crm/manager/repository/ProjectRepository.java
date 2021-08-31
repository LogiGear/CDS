package com.logigear.crm.manager.repository;

import com.logigear.crm.manager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface ProjectRepository extends JpaRepository<Project,Long>,
        QuerydslPredicateExecutor<Project> {

    Set<Project> findByIdIn(Set<Long> id);
}
