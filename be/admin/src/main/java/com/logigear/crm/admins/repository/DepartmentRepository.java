package com.logigear.crm.admins.repository;

import java.util.List;

import com.logigear.crm.admins.model.Department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    <T> List<T> findBy(Class<T> type);
}
