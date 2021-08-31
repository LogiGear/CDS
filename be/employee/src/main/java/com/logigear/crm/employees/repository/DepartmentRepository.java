package com.logigear.crm.employees.repository;

import com.logigear.crm.employees.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>,
        QuerydslPredicateExecutor<Department> {
    /**
     * Get the current department with the provided name
     * @param name the provided name associated with that department
     * @return the current Department instance wrapped in an Optional class
     */
    Optional<Department> findDepartmentByName(String name);


    <T> List<T> findBy(Class<T> type);

    <T> List<T> findAllByIsDeleted(Class<T> type,boolean isDeleted);
    @Query("SELECT dep FROM Department dep WHERE dep.id = :id AND dep.isDeleted = :isDeleted")
    <T> Optional<T> findByIdAndDeleted(Long id,boolean isDeleted,Class<T> type);

    <T> List<T> findAllByParentDepartmentIsNullAndIsDeleted(Class<T> type,boolean isDeleted);
}
