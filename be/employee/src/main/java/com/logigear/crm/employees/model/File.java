package com.logigear.crm.employees.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "resumes", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class File implements Serializable {
	@Id
	@SequenceGenerator(name = "cv_id_seq", sequenceName = "cv_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cv_id_seq")
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String fileName;

	@Column(name = "type")
	private String fileType;

	@Column(name = "status")
	private String status;

	@Column(name = "note")
	private String note;

	@Column(name = "upload_at")
	private Instant uploadAt;

	@Column(name = "upload_by")
	private Long uploadBy;

	// @OneToOne(mappedBy = "file", optional = true)
	// @JoinColumn(name = "id", referencedColumnName = "id", insertable = false,
	// updatable = false)
	// private EmployeeDetails employeeDetails;

}
