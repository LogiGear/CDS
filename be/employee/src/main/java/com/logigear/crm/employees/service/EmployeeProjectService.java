package com.logigear.crm.employees.service;


import com.logigear.crm.employees.exception.ResourceNotFoundException;
import com.logigear.crm.employees.model.composite.EmployeeDetails_Project;
import com.logigear.crm.employees.repository.EmployeeProjectRepository;
import com.logigear.crm.employees.repository.EmployeeRepository;
import com.logigear.crm.employees.response.EmployeeDetailsProjectDTO;
import com.logigear.crm.employees.util.MessageUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmployeeProjectService {


    private final EmployeeProjectRepository employeeProjectRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeProjectService(EmployeeProjectRepository employeeProjectRepository,
                                  EmployeeRepository employeeRepository) {
        this.employeeProjectRepository = employeeProjectRepository;
        this.employeeRepository = employeeRepository;
    }


    public EmployeeDetails_Project getEmployeeDetailsProjectByEmployeeId(Long id) {
        List<EmployeeDetails_Project> employeeDetails_projectList =
                employeeProjectRepository.findAllEmployeeId(id);

        if(employeeDetails_projectList.isEmpty()){
            EmployeeDetails_Project employeeDetails_project = new EmployeeDetails_Project();
            employeeDetails_project.setEmployees(employeeRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(
                            MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
            ));
            employeeDetails_projectList.add(employeeDetails_project);
        }
            return employeeDetails_projectList.get(employeeDetails_projectList.size() - 1);


    }
    
    public EmployeeDetailsProjectDTO getProjectDetailByEmployee(Long id) {
    	EmployeeDetails_Project employeeDetails = getEmployeeDetailsProjectByEmployeeId(id);
    	ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(employeeDetails,EmployeeDetailsProjectDTO.class);
    }


}
