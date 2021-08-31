package com.logigear.crm.employees.repository;

import java.util.List;
import java.util.Optional;

import com.logigear.crm.employees.model.Project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    /**
     * Get the current project instance with the provide name
     * @param name Then provided name associated with the project
     * @return The current Project instance wrapped in an Optional class
     */
    Optional<Project> findProjectByName(String name);

    <T> List<T> findBy(Class<T> type);
    <T> List<T> findAllByIsDeleted(Class<T> type,boolean isDeleted);
    
}
