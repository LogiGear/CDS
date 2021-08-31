package com.logigear.crm.manager.repository;

import com.logigear.crm.manager.model.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDetails,Long>,
        QuerydslPredicateExecutor<EmployeeDetails>
{

    @Override
    Optional<EmployeeDetails> findById(Long id);

    <T> List<T> findBy(Class<T> type);

    @Query("SELECT emp FROM EmployeeDetails  emp WHERE emp.cdm.id = :id OR emp.manager.id = :id")
    <T> List<T> findByManagerAndCdm(Long id,Class<T> type);
}
