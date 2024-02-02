package org.devridge.api.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOccupation is a Querydsl query type for Occupation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOccupation extends EntityPathBase<Occupation> {

    private static final long serialVersionUID = -106722134L;

    public static final QOccupation occupation1 = new QOccupation("occupation1");

    public final org.devridge.common.dto.QBaseEntity _super = new org.devridge.common.dto.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final StringPath occupation = createString("occupation");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QOccupation(String variable) {
        super(Occupation.class, forVariable(variable));
    }

    public QOccupation(Path<? extends Occupation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOccupation(PathMetadata metadata) {
        super(Occupation.class, metadata);
    }

}

