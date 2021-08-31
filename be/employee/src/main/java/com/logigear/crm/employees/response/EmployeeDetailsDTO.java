package com.logigear.crm.employees.response;

import java.time.Instant;

import com.logigear.crm.employees.model.Department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String jobTitle;
    private EmployeeDetailsDTO manager;
    private EmployeeDetailsDTO cdm;
}
