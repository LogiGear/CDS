package com.logigear.crm.template.model.key;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EmployeeDetails_Project_Key implements Serializable {
    @Column(name = "employee_id")
    Long employeeId;

    @Column(name = "project_id")
    Long projectId;


}
