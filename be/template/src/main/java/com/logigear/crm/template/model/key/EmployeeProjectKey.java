package com.logigear.crm.template.model.key;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EmployeeProjectKey implements Serializable {
    @Column(name = "employee_id")
    Long employeeId;

    @Column(name = "project_id")
    Long projectId;
}
