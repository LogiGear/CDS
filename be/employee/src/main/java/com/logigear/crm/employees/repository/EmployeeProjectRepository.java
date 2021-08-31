package com.logigear.crm.employees.repository;

import java.util.List;

import com.logigear.crm.employees.model.composite.EmployeeDetails_Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeDetails_Project, Long> {

    @Query("SELECT emp FROM EmployeeDetails_Project emp WHERE emp.employees.id = :id")
    List<EmployeeDetails_Project> findAllEmployeeId(Long id);
}
