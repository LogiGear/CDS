package com.logigear.crm.manager.response;

import org.springframework.beans.factory.annotation.Value;

public interface DepartmentResponse {
    @Value("#{target.id}")
    Long getId();

    String getName();

    void setId(Long id);
    void setName(String name);
}
