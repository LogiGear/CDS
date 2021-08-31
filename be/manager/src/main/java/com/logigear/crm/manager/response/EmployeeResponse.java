package com.logigear.crm.manager.response;

import com.logigear.crm.manager.model.Department;
import com.logigear.crm.manager.model.Project;
import com.logigear.crm.manager.model.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;
public interface EmployeeResponse {

    @Value("#{target.id}")
    Long getId();
    
    String getFullName();
    String getGender();
    String getDegree();
    String getMajor();
    Department getDepartment();
    String getJobTitle();
    Set<Project> getProjects();
    User getUser();

    void setId(Long id);
    void setFullName(String fullName);
    void setGender(String gender);
    void setDegree(String degree);
    void setMajor(String major);
    void setDepartment(Department department);
    void setJobTitle(String jobTitle);
    void setProjects(Set<Project> projects);
    void setUser(User user);
}
