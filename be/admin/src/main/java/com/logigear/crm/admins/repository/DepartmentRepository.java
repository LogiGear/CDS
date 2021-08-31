package com.logigear.crm.admins.repository;

import com.logigear.crm.admins.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    <T> List<T> findBy(Class<T> type);
}
