package org.devridge.api.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractTimeEntity is a Querydsl query type for AbstractTimeEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAbstractTimeEntity extends EntityPathBase<AbstractTimeEntity> {

    private static final long serialVersionUID = 977804574L;

    public static final QAbstractTimeEntity abstractTimeEntity = new QAbstractTimeEntity("abstractTimeEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAbstractTimeEntity(String variable) {
        super(AbstractTimeEntity.class, forVariable(variable));
    }

    public QAbstractTimeEntity(Path<? extends AbstractTimeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractTimeEntity(PathMetadata metadata) {
        super(AbstractTimeEntity.class, metadata);
    }

}

