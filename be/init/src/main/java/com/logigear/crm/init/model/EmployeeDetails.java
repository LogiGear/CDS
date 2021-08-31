package com.logigear.crm.init.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "EmployeeDetails")
@Table(name = "employeesdetails", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) }, schema = "public")
@Getter
@Setter
public class EmployeeDetails extends ModelSuperclass {
    @Id
    @SequenceGenerator(name = "employeedetails_id_seq", sequenceName = "employeedetails_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeedetails_id_seq")
    @Column(name = "id")
    private Long id;

    // @NotBlank
    @Size(max = 40)
    @Column(name = "full_name")
    private String fullName;

    // Personal Info
    @Column(name = "birth_day")
    private Instant birthDay;

    // @NotBlank
    @Column(name = "place_of_birth")
    private String placeOfBirth;

    // @NotBlank
    @Size(max = 10)
    @Column(name = "gender")
    private String gender;

    @Column(name = "id_number")
    private Long idNumber;

    @Column(name = "issue_date")
    private Instant issueDate;

    // @NotBlank
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
    // @NotBlank
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private Long bankAccount;

    @Column(name = "image")
    private String image;

    // @Column(name = "cv")
    // private String cv;

    // Education
    // @NotEmpty
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

    // @NotBlank
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "job_title")
    private String jobTitle;

    // ?? id of Manager
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private EmployeeDetails manager;

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private Set<EmployeeDetails> subordinatesManager;

    // ?? id of CDM
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "cdm_id")
    private EmployeeDetails cdm;

    @OneToMany(mappedBy = "cdm")
    @JsonIgnore
    private Set<EmployeeDetails> subordinatesCdm;

    @OneToOne(optional = false,cascade = {CascadeType.ALL})
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @OneToOne(optional = true,cascade = {CascadeType.ALL})
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private File file;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employeedetails_projects", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<>();

}
