package com.logigear.crm.authenticate.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "employeesdetails", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetails extends ModelSuperclass{
    @Id
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(name = "full_name")

    private String fullName;
    @Column(name = "id_number")
    private Long idNumber;

    // At company
    @Column(name = "employeeid")
    private int employeeID;

    // Personal Info
    @Column(name = "birth_day")
    private Instant birthDay;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Size(max = 10)
    @Column(name = "gender")
    private String gender;

    @Column(name = "issue_date")
    private Instant issueDate;

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
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private Long bankAccount;

    @Column(name = "image")
    private String image;

    // Education
    @Column(name = "degree")
    private String degree;

    @Column(name = "major")
    private String major;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "contracted_date")
    private Instant contractedDate;

    @Column(name = "job_title")
    private String jobTitle;

}
