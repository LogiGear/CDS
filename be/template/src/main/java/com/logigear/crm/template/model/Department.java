package com.logigear.crm.template.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="Department")
@Table(name = "departments", schema = "public")
@Getter
@Setter
public class Department extends ModelSuperclass {
	
	@Id
	@SequenceGenerator(name="departments_id_seq",
			sequenceName="departments_id_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="departments_id_seq")
	@Column(name="id")
	private Long id;

	@Column(name = "name")
	private String name;

	    
}
