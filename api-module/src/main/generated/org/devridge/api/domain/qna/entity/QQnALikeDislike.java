package org.devridge.api.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnALikeDislike is a Querydsl query type for QnALikeDislike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnALikeDislike extends EntityPathBase<QnALikeDislike> {

    private static final long serialVersionUID = 655920259L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnALikeDislike qnALikeDislike = new QQnALikeDislike("qnALikeDislike");

    public final org.devridge.common.dto.QBaseTimeEntity _super = new org.devridge.common.dto.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.devridge.api.domain.qna.entity.id.QQnALikeDislikeId id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final EnumPath<org.devridge.api.domain.qna.dto.type.LikeStatus> status = createEnum("status", org.devridge.api.domain.qna.dto.type.LikeStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQnALikeDislike(String variable) {
        this(QnALikeDislike.class, forVariable(variable), INITS);
    }

    public QQnALikeDislike(Path<? extends QnALikeDislike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnALikeDislike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnALikeDislike(PathMetadata metadata, PathInits inits) {
        this(QnALikeDislike.class, metadata, inits);
    }

    public QQnALikeDislike(Class<? extends QnALikeDislike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new org.devridge.api.domain.qna.entity.id.QQnALikeDislikeId(forProperty("id"), inits.get("id")) : null;
    }

}

