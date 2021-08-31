package com.logigear.crm.employees.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserUpdateRequest {

    public long id;

    public Set<Long> roles;
}
