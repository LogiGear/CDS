package com.logigear.crm.employees.service;

import com.logigear.crm.employees.model.Department;
import com.logigear.crm.employees.repository.DepartmentRepository;
import com.logigear.crm.employees.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    public void retrieveAllDepartment() {
        Department dep1 = new Department();
        Department dep2 = new Department();
        dep1.setId(1L);dep1.setName("Admin");
        dep2.setId(2L);dep2.setName("User");
        when(departmentRepository.findBy(Department.class)).thenReturn(Arrays
                .asList(dep1,dep2));
        List<Department> dep = departmentService.findAll(Department.class);
        assertEquals(1L, dep.get(0).getId());
        assertEquals("User", dep.get(1).getName());
    }

}
