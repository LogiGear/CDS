package com.logigear.crm.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logigear.crm.employees.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
