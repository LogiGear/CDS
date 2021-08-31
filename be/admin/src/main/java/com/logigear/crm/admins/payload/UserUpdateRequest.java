package com.logigear.crm.admins.payload;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateRequest {

    public long id;

    public Set<Long> roles;
}
