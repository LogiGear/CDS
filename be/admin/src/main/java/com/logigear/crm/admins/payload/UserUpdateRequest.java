package com.logigear.crm.admins.payload;

import com.logigear.crm.admins.model.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserUpdateRequest {

    public long id;

    public Set<Long> roles;
}
