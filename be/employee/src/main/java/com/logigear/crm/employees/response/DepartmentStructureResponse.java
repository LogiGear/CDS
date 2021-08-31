package com.logigear.crm.employees.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.annotation.Value;

public interface DepartmentStructureResponse {
    @Value("#{target.id}")
    Long getId();

    String getName();

    EmployeeStructureResponse getManager();

    @JsonProperty("children")
    Set<DepartmentStructureResponse> getChildDepartment();

    void setId(Long id);
    void setName(String name);
    void setChildDepartment(Set<DepartmentStructureResponse> departments);
    void setManager(EmployeeStructureResponse manager);
}
