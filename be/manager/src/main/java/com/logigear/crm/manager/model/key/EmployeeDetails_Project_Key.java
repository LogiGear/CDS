package com.logigear.crm.manager.model.key;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EmployeeDetails_Project_Key implements Serializable {
    @Column(name = "employee_id")
    Long employeeId;

    @Column(name = "project_id")
    Long projectId;
}
