package com.logigear.crm.manager.response;

import com.logigear.crm.manager.model.Department;
import lombok.*;

import java.time.Instant;

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
    private UserDTO user;
}
