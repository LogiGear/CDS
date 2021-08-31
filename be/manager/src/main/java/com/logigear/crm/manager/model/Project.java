package com.logigear.crm.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
@QueryEntity
@Entity(name="Project")
@Table(name = "projects", schema = "public")
@Getter
@Setter
@NoArgsConstructor
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

	@ManyToMany(
			cascade = { CascadeType.ALL },
			fetch = FetchType.LAZY,
			mappedBy = "projects"
	)
	@JsonIgnore
	private Set<EmployeeDetails> employeeDetails;
	    
}
