package com.logigear.crm.employees.service;

import java.util.List;

public interface ProjectService {
    <T> List<T> findAll(Class<T> type);
    <T> List<T> findAllByIsDeleted(Class<T> type,boolean isDeleted);
}
