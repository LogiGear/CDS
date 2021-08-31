package com.logigear.crm.admins.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="Department")
@Table(name = "departments", schema = "public")
@Getter
@Setter
@NoArgsConstructor
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
