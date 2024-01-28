package org.devridge.api.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnAScrap is a Querydsl query type for QnAScrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnAScrap extends EntityPathBase<QnAScrap> {

    private static final long serialVersionUID = -1671187834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnAScrap qnAScrap = new QQnAScrap("qnAScrap");

    public final org.devridge.common.dto.QBaseTimeEntity _super = new org.devridge.common.dto.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.devridge.api.domain.qna.entity.id.QQnAScrapId id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQnAScrap(String variable) {
        this(QnAScrap.class, forVariable(variable), INITS);
    }

    public QQnAScrap(Path<? extends QnAScrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnAScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnAScrap(PathMetadata metadata, PathInits inits) {
        this(QnAScrap.class, metadata, inits);
    }

    public QQnAScrap(Class<? extends QnAScrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new org.devridge.api.domain.qna.entity.id.QQnAScrapId(forProperty("id"), inits.get("id")) : null;
    }

}

