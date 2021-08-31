package com.logigear.crm.employees.response;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeUpdateRequest {
    private Long id;
    private String email;
    private int employeeID;
    private Long manager;
    private Long cdm;
    private String departmentName;
    private String jobTitle;
    private String projectName;
    private Instant startDate;
    private Long department;
    private Long project;
    private Set<Long> projects;
}
