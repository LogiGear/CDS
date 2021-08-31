package com.logigear.crm.manager.model.query;

import com.logigear.crm.manager.model.EmployeeDetails;
import com.logigear.crm.manager.model.Project;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QEmployeeDetails is a Querydsl query type for EmployeeDetails
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEmployeeDetails extends EntityPathBase<EmployeeDetails> {

    private static final long serialVersionUID = 421093137L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployeeDetails employeeDetails = new QEmployeeDetails("employeeDetails");

    public final QModelSuperclass _super = new QModelSuperclass(this);

    public final StringPath address = createString("address");

    public final NumberPath<Long> bankAccount = createNumber("bankAccount", Long.class);

    public final StringPath bankName = createString("bankName");

    public final DateTimePath<java.time.Instant> birthDay = createDateTime("birthDay", java.time.Instant.class);

    public final QEmployeeDetails cdm;

    public final StringPath cellPhone = createString("cellPhone");

    public final DateTimePath<java.time.Instant> contractedDate = createDateTime("contractedDate", java.time.Instant.class);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath degree = createString("degree");

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final QDepartment department;

    public final NumberPath<Integer> employeeID = createNumber("employeeID", Integer.class);

    public final StringPath fullName = createString("fullName");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> idNumber = createNumber("idNumber", Long.class);

    public final StringPath image = createString("image");

    public final NumberPath<Long> insuranceBookNo = createNumber("insuranceBookNo", Long.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final DateTimePath<java.time.Instant> issueDate = createDateTime("issueDate", java.time.Instant.class);

    public final StringPath jobTitle = createString("jobTitle");

    public final StringPath major = createString("major");

    public final QEmployeeDetails manager;

    public final StringPath marriedStatus = createString("marriedStatus");

    public final StringPath placeOfBirth = createString("placeOfBirth");

    public final SetPath<Project, QProject> projects = this.<Project, QProject>createSet("projects", Project.class, QProject.class, PathInits.DIRECT2);

    public final StringPath race = createString("race");

    public final StringPath religion = createString("religion");

    public final DateTimePath<java.time.Instant> startDate = createDateTime("startDate", java.time.Instant.class);

    public final SetPath<EmployeeDetails, QEmployeeDetails> subordinatesCdm = this.<EmployeeDetails, QEmployeeDetails>createSet("subordinatesCdm", EmployeeDetails.class, QEmployeeDetails.class, PathInits.DIRECT2);

    public final SetPath<EmployeeDetails, QEmployeeDetails> subordinatesManager = this.<EmployeeDetails, QEmployeeDetails>createSet("subordinatesManager", EmployeeDetails.class, QEmployeeDetails.class, PathInits.DIRECT2);

    public final NumberPath<Long> taxID = createNumber("taxID", Long.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final QUser user;

    public QEmployeeDetails(String variable) {
        this(EmployeeDetails.class, forVariable(variable), INITS);
    }

    public QEmployeeDetails(Path<? extends EmployeeDetails> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmployeeDetails(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmployeeDetails(PathMetadata metadata, PathInits inits) {
        this(EmployeeDetails.class, metadata, inits);
    }

    public QEmployeeDetails(Class<? extends EmployeeDetails> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cdm = inits.isInitialized("cdm") ? new QEmployeeDetails(forProperty("cdm"), inits.get("cdm")) : null;
        this.department = inits.isInitialized("department") ? new QDepartment(forProperty("department")) : null;
        this.manager = inits.isInitialized("manager") ? new QEmployeeDetails(forProperty("manager"), inits.get("manager")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

