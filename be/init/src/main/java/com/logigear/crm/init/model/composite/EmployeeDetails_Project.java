package com.logigear.crm.init.model.composite;

import com.logigear.crm.init.model.EmployeeDetails;
import com.logigear.crm.init.model.Project;
import com.logigear.crm.init.model.key.EmployeeDetails_Project_Key;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="EmployeeDetails_Project")
@Table(name = "employeedetails_projects")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeDetails_Project {

    @EmbeddedId
    @Column(
            name="id",
            updatable = false
    )
    private EmployeeDetails_Project_Key id;



    @ManyToOne
    @MapsId("project_id")
    @JoinColumn(name = "project_id")
    private Project projects;

    @ManyToOne
    @MapsId("employee_id")
    @JoinColumn(name = "employee_id")
    private EmployeeDetails employees;

}
