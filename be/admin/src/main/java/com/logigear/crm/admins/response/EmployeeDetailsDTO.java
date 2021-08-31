package com.logigear.crm.admins.response;


import lombok.*;

import java.time.Instant;
import java.util.Set;

import com.logigear.crm.admins.model.Department;
import com.logigear.crm.admins.model.EmployeeDetails;
import com.logigear.crm.admins.model.Project;
import com.logigear.crm.admins.model.RoleName;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDetailsDTO {
	private Long id;
    private String fullName;
    private Instant birthDay;
    private String placeOfBirth;
    private String gender;
    private Long idNumber;
    private Instant issueDate;
    private String race;
    private String religion;
    private String marriedStatus;
    private String cellPhone;
    private Long taxID;
    private Long insuranceBookNo;
    private String address;
    private String bankName;
    private Long bankAccount;
    private String image;
    private String degree;
    private String major;
    private int employeeID;
    private Instant startDate;
    private Instant contractedDate;
    private Department department;
    private Set<Project> projects;
    private String jobTitle;
    private EmployeeDetails manager;
    private EmployeeDetails cdm;
    private Set<EmployeeDetails> subordinatesManager;
    private Set<EmployeeDetails> subordinatesCdm;
}
