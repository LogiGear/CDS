package com.logigear.crm.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;
@QueryEntity
@Entity(name="Role")
@Table(name = "roles", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Role extends ModelSuperclass {
	
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
	@JsonIgnore
	private Set<User> users;
	    
}
