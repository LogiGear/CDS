package com.logigear.crm.employees.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.logigear.crm.employees.model.Department;
import com.logigear.crm.employees.model.EmployeeDetails;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

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
