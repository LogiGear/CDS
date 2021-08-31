package com.logigear.crm.employees.service.impl;

import java.util.List;

import com.logigear.crm.employees.repository.ProjectRepository;
import com.logigear.crm.employees.service.ProjectService;

import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }
    @Override
    public <T> List<T> findAll(Class<T> type) {
        return projectRepository.findBy(type);
    }

    @Override
    public <T> List<T> findAllByIsDeleted(Class<T> type, boolean isDeleted) {
        return projectRepository.findAllByIsDeleted(type,isDeleted);
    }
}
