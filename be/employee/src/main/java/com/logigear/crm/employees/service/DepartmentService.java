package com.logigear.crm.employees.service;

import java.util.List;

public interface DepartmentService {
    <T> List<T> findAll(Class<T> type);
    <T> List<T> findAllByIsDeleted(Class<T> type,boolean isDeleted);
    <T> T findDepartment(Class<T> type,boolean isDeleted,Long id);
    <T> List<T> findAllDepartment(Class<T> type,boolean isDeleted);
}
