package com.logigear.crm.manager.service;

import java.util.List;

public interface DepartmentService {
    <T> List<T> findAll(Class<T> type);
}
