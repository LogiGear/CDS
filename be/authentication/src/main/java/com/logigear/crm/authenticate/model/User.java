package com.logigear.crm.authenticate.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.logigear.crm.authenticate.model.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"email"})
})
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
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

	@Column(name="is_deleted")
	private boolean isDeleted;

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
