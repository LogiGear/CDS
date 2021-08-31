package com.logigear.crm.employees.service.impl;

import java.util.List;

import com.logigear.crm.employees.exception.ResourceNotFoundException;
import com.logigear.crm.employees.repository.DepartmentRepository;
import com.logigear.crm.employees.service.DepartmentService;
import com.logigear.crm.employees.util.MessageUtil;

import org.springframework.stereotype.Service;

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

    @Override
    public <T> List<T> findAllByIsDeleted(Class<T> type, boolean isDeleted) {
        return departmentRepository.findAllByIsDeleted(type,isDeleted);
    }
    @Override
    public<T> T findDepartment(Class<T> type,boolean isDeleted,Long id){
        return departmentRepository.findByIdAndDeleted(id,isDeleted,type).orElseThrow(
                () -> new ResourceNotFoundException(
                        MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
        );
    }

    public <T> List<T> findAllDepartment(Class<T> type,boolean isDeleted){
        return departmentRepository.findAllByParentDepartmentIsNullAndIsDeleted(type,isDeleted);
    }




}
