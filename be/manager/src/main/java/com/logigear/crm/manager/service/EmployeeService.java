package com.logigear.crm.manager.service;

import com.logigear.crm.manager.model.EmployeeDetails;
import com.logigear.crm.manager.payload.EmployeePayload;
import com.logigear.crm.manager.response.EmployeeDetailsDTO;

import java.util.List;

public interface EmployeeService {
    <T> List<T> findAll(Class<T> type);
    List<EmployeeDetails> findFilter(String name,String [] major);
    <T> List<T> findAll(Long id,Class<T> type);
    EmployeeDetails updateEmployeeDetails(EmployeeDetailsDTO req);
    <T> List<T> findAllExcludeAdmin(Long id, Class<T> type);
    <T> List<T> findAllExcludeAdmin(Class<T> type);
    <T> T updateEmployee(EmployeePayload source, Long id,Class<T> type);
}
