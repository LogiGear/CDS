package com.logigear.crm.employees.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "public")
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	
	@Id
	@SequenceGenerator(name="roles_id_seq",
			sequenceName="roles_id_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="roles_id_seq")
	@Column(name="id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private RoleName name;
	
	@ManyToMany(
            cascade = { CascadeType.ALL },
            fetch = FetchType.LAZY,
            mappedBy = "roles"
    )
	
	private Set<User> users;
}
