package com.logigear.crm.admins.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EmployeePayload {
    public long id;

    public Set<Long> projects;
    public Long department;
    
    Long cdmId;
    Long managerId;
}
