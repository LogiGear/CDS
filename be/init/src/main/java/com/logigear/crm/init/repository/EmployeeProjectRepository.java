package com.logigear.crm.init.repository;

import com.logigear.crm.init.model.composite.EmployeeDetails_Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeDetails_Project, Long> {
}
