package com.logigear.crm.admins.repository;

import java.util.Set;

import com.logigear.crm.admins.model.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long>{

    Set<Project> findByIdIn(Set<Long> id);
}
