package com.logigear.crm.admins.model.composite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.logigear.crm.admins.model.key.Role_User_Key;
import com.logigear.crm.admins.model.Role;
import com.logigear.crm.admins.model.User;

import javax.persistence.*;

@Entity(name="Users_Roles")
@Table(name = "users_roles")
@Getter
@Setter
@NoArgsConstructor
public class Role_User {
	@EmbeddedId
    @Column(
            name="id",
            updatable = false
    )
    private Role_User_Key id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User users;

    @ManyToOne
    @MapsId("role_id")
    @JoinColumn(name = "role_id")
    private Role roles;
}
