package com.logigear.crm.employees.repository;

import com.logigear.crm.employees.model.EmployeeDetails;
import com.logigear.crm.employees.model.composite.EmployeeDetails_Project;
import com.logigear.crm.employees.model.key.EmployeeDetails_Project_Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeDetails_Project, Long> {

    @Query("SELECT emp FROM EmployeeDetails_Project emp WHERE emp.employees.id = :id")
    List<EmployeeDetails_Project> findAllEmployeeId(Long id);
}
