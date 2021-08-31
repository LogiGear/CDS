package com.logigear.crm.template.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity(name="Role")
@Table(name = "roles", schema = "public")
@Getter
@Setter
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
	    
}
