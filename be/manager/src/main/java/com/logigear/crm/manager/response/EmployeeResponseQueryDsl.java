package com.logigear.crm.manager.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeResponseQueryDsl {

    protected class User {
        public String email;

        public User(String email) {
            this.email = email;
        }
    }

    protected class Department {
        public Long id;
        public String name;

        public Department(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private Long id;
    private String gender;
    private String fullName;
    private String jobTitle;
    private String major;
    private User user;
    private Department department;

    public EmployeeResponseQueryDsl(Long id, String fullName, String gender, String jobTitle, String major,
            String userEmail, Long deparmentId, String departmentName) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.jobTitle = jobTitle;
        this.major = major;
        this.user = new User(userEmail);
        this.department = new Department(deparmentId, departmentName);
    }

}
