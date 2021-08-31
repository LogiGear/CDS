package com.logigear.crm.init.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Department")
@Table(name = "departments", schema = "public")
@Getter
@Setter
public class Department extends ModelSuperclass {

	@Id
	@SequenceGenerator(name = "departments_id_seq", sequenceName = "departments_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_id_seq")
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToOne(optional = true)
	@JoinColumn(name = "manager_id", referencedColumnName = "id")
	@JsonIgnore
	private EmployeeDetails manager;

	@ManyToOne
	@JoinColumn(name = "parent_department_id")
	private Department parentDepartment;

	@OneToMany(mappedBy = "parentDepartment")
	@JsonIgnore
	private Set<Department> childDepartment;

}
