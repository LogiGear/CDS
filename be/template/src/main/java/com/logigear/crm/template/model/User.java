package com.logigear.crm.template.model;

import com.logigear.crm.template.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity(name="User")
@Table(name = "users", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"email"})
})
@Getter
@Setter
public class User extends DateAudit {

	private static final long serialVersionUID = 5644797795094164062L;

	@Id
	@SequenceGenerator(name="users_id_seq",
			sequenceName="users_id_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="users_id_seq")
	@Column(name="id")
    private Long id;

	@OneToOne( mappedBy = "user")
	private EmployeeDetails employee;

	@NotBlank
    private String name;
	
	@NotBlank
	@Size(max = 40)
	@Email
	private String email;
	
	@NotBlank
    @Size(max = 100)
    private String password;

	@Column(name="email_token")
	private String emailToken;
	
	@Column(name="password_reset_token")
	private String passwordResetToken;

	@Enumerated(EnumType.STRING)
	@Column(length = 60)
	private UserStatus status;

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	public User(User user) { 
	    this.id = user.id;
	    this.email = user.email;
	    this.password = user.password;
	    this.roles = user.roles;
	    this.setCreatedAt(user.getCreatedAt());
	    this.setUpdatedAt(user.getUpdatedAt());
	}
}
