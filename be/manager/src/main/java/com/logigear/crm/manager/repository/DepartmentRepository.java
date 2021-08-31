package com.logigear.crm.manager.repository;

import com.logigear.crm.manager.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long>,
        QuerydslPredicateExecutor<Department> {
    <T> List<T> findBy(Class<T> type);
}
