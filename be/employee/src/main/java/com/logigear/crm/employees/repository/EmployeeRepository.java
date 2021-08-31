package com.logigear.crm.employees.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.logigear.crm.employees.model.EmployeeDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmployeeRepository extends JpaRepository<EmployeeDetails, Long>,
        QuerydslPredicateExecutor<EmployeeDetails> {
    /**
     * Get the current employee instance from database with the provided employee id.
     *
     * @param id The provided id associated with that employee.
     * @return The current EmployeeDetails instance wrapped in an Optional class.
     **/
    Optional<EmployeeDetails> findById(Long id);

    @Query("SELECT emp FROM EmployeeDetails emp, EmployeeDetails emp2 WHERE emp2.id = :id AND (emp.department.id = emp2.department.id)" +
            "AND emp.isDeleted = :deleted")
    <T> List<T> findByDepartmentIdAndDeleted(Long id,boolean deleted,Class<T> type);




    /**
     * Get all employees from database
     *
     * @return The list of EmployeeDetails
     **/
    List<EmployeeDetails> findAll();

    /**
     * Get all employee ids from database
     *
     * @return The list of EmployeeId wrapped in Optional class
     * @author bang.ngo
     **/
    @Query("SELECT e.id FROM EmployeeDetails e")
    Optional<List<String>> findAllEmployeeId();

    /**
     * Get the current employee instance form database with the provided employee's full name
     * @param fullName The provided full name associated with that employee
     * @return The current EmployeeDetails instance wrapped in an Optional class
     */
    Optional<EmployeeDetails> findEmployeeDetailsByFullName(String fullName);

    /**
     * Get the current Employee instance from database with the provided employee's id
     * @param employeeId The provided employee id associated with that employee
     * @return The current Employee instance wrapped in an Optional class
     */
    Optional<EmployeeDetails> findByEmployeeID(int employeeId);
}
