package com.logigear.crm.manager.payload;

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
}
