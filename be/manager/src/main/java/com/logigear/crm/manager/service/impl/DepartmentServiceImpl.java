package com.logigear.crm.manager.service.impl;

import com.logigear.crm.manager.repository.DepartmentRepository;
import com.logigear.crm.manager.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }
    @Override
    public <T> List<T> findAll(Class<T> type) {
        return departmentRepository.findBy(type);
    }
}
