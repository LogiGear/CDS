package com.logigear.crm.employees.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="Project")
@Table(name = "projects", schema = "public")
@Getter
@Setter
public class Project extends ModelSuperclass {
	
	@Id
	@SequenceGenerator(name="projects_id_seq",
			sequenceName="projects_id_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="projects_id_seq")
	@Column(name="id")
	private Long id;

	@Column(name = "name")
	private String name;
	    
}
