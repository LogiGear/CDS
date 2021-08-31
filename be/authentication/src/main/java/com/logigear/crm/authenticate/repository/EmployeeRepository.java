package com.logigear.crm.authenticate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.logigear.crm.authenticate.model.EmployeeDetails;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeDetails, Long> {
    /**
     * Get the current employee instance from database with the provided employee id.
     *
     * @param id The provided id associated with that employee.
     * @return The current EmployeeDetails instance wrapped in an Optional class.
     **/
    Optional<EmployeeDetails> findById(Long id);

    /**
     * Get all employees from database
     *
     * @return The list of EmployeeDetails
     **/
    List<EmployeeDetails> findAll();

    @Query("SELECT MAX(e.employeeID) FROM EmployeeDetails e")
    int findMaxEmployeeId();

    /**
     * Get all employee ids from database
     *
     * @return The list of EmployeeId wrapped in Optional class
     * @author bang.ngo
     **/
    @Query("SELECT e.id FROM EmployeeDetails e")
    Optional<List<String>> findAllEmployeeId();
}
