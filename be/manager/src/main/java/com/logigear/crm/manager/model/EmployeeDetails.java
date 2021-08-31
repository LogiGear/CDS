package com.logigear.crm.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@QueryEntity
@Entity(name="EmployeeDetails")
@Table(name = "employeesdetails", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})
})
@Getter
@Setter
@NoArgsConstructor
public class EmployeeDetails extends ModelSuperclass {
    @Id
    @SequenceGenerator(name="employeedetails_id_seq",
            sequenceName="employeedetails_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="employeedetails_id_seq")
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(name = "full_name")
    private String fullName;

    // Personal Info
    @Column(name = "birth_day")
    private Instant birthDay;

//    @NotBlank
    @Column(name = "place_of_birth")
    private String placeOfBirth;

//    @NotBlank
    @Size(max = 10)
    @Column(name = "gender")
    private String gender;

    @Column(name = "id_number")
    private Long idNumber;

    @Column(name = "issue_date")
    private Instant issueDate;

//    @NotBlank
    @Size(max = 20)
    @Column(name = "race")
    private String race;

    @Size(max = 20)
    @Column(name = "religion")
    private String religion;

    @Column(name = "married_status")
    private String marriedStatus;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "taxid")
    private Long taxID;

    @Column(name = "insurance_book_no")
    private Long insuranceBookNo;

    @Column(name = "address")
    private String address;

    // Payment info
//    @NotBlank
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private Long bankAccount;

    @Column(name = "image")
    private String image;

    // Education
//    @NotEmpty
    @Column(name = "degree")
    private String degree;

    @Column(name = "major")
    private String major;

    // At company
    @Column(name = "employeeid")
    private int employeeID;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "contracted_date")
    private Instant contractedDate;

//    @NotBlank
    @ManyToOne
    private Department department;

    @Column(name = "job_title")
    private String jobTitle;

    // ?? id of Manager
    @ManyToOne
    @JoinColumn(name="manager_id")
    private EmployeeDetails manager;

    @OneToMany(mappedBy="manager")
    @JsonIgnore
    private Set<EmployeeDetails> subordinatesManager;

    // ?? id of CDM
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="cdm_id")
    private EmployeeDetails cdm;

    @OneToMany(mappedBy="cdm")
    @JsonIgnore
    private Set<EmployeeDetails> subordinatesCdm;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employeedetails_projects",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<>();


    @OneToOne(optional = false)
    @JoinColumn(name = "id")
    @MapsId
    private User user;

}
