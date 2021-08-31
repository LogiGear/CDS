package com.logigear.crm.employees.response;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.logigear.crm.employees.model.Department;
import com.logigear.crm.employees.model.EmployeeDetails;
import com.logigear.crm.employees.model.Project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeDetailsProjectDTO {
//    private EmployeeDetails employees;
//    private Project projects;
    @JsonProperty("projects")
    private Set<Project> employeesProjects;
    @JsonProperty("id")
    private Long employeesId;
    @JsonProperty("fullName")
    private String employeesFullName;
    @JsonProperty("gender")
    private String employeesGender;
    @JsonProperty("idNumber")
    private Long employeesIdNumber;
    @JsonProperty("birthDay")
    private Instant employeesBirthDay;
    @JsonProperty("religion")
    private String employeesReligion;
    @JsonProperty("cellPhone")
    private String employeesCellPhone;
    @JsonProperty("placeOfBirth")
    private String employeesPlaceOfBirth;
    @JsonProperty("address")
    private String employeesAddress;
    @JsonProperty("bankName")
    private String employeesBankName;
    @JsonProperty("bankAccount")
    private Long employeesBankAccount;
    @JsonProperty("image")
    private String employeesImage;
    @JsonProperty("employeeID")
    private int employeesEmployeeID;
    @JsonProperty("startDate")
    private Instant employeesStartDate;
    @JsonProperty("department")
    private Department employeesDepartment;
    @JsonProperty("jobTitle")
    private String employeesJobTitle;
    @JsonProperty("email")
    private String employeesUserEmail;

    @JsonProperty("manager")
    private EmployeeDetails employeesManager;
    @JsonProperty("cdm")
    private EmployeeDetails employeesCdm;

    @JsonProperty("project")
    private Long projectsId;





}
