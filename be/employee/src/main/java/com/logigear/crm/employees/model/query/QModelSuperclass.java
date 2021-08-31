package com.logigear.crm.employees.model.query;

import com.logigear.crm.employees.model.ModelSuperclass;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QModelSuperclass is a Querydsl query type for ModelSuperclass
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QModelSuperclass extends BeanPath<ModelSuperclass> {

    private static final long serialVersionUID = -1927517349L;

    public static final QModelSuperclass modelSuperclass = new QModelSuperclass("modelSuperclass");

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final BooleanPath deleted = createBoolean("deleted");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final DateTimePath<java.time.Instant> updatedAt = createDateTime("updatedAt", java.time.Instant.class);

    public QModelSuperclass(String variable) {
        super(ModelSuperclass.class, forVariable(variable));
    }

    public QModelSuperclass(Path<? extends ModelSuperclass> path) {
        super(path.getType(), path.getMetadata());
    }

    public QModelSuperclass(PathMetadata metadata) {
        super(ModelSuperclass.class, metadata);
    }

}

