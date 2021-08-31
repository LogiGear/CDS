package com.logigear.crm.manager.model.composite;

import com.logigear.crm.manager.model.Role;
import com.logigear.crm.manager.model.User;
import com.logigear.crm.manager.model.key.User_Role_Key;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="User_Role")
@Table(name = "users_roles")
@Getter
@Setter
@NoArgsConstructor
public class User_Role {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name="id",
            updatable = false
    )
    private User_Role_Key id;

    @SuppressWarnings("JpaModelReferenceInspection")
    @ManyToOne
    @MapsId("users_id")
    @JoinColumn(name = "users_id")
    private User users;

    @SuppressWarnings("JpaModelReferenceInspection")
    @ManyToOne
    @MapsId("roles_id")
    @JoinColumn(name = "roles_id")
    private Role roles;

}
