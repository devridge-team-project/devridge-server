package org.devridge.api.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentLikeDislike is a Querydsl query type for CommentLikeDislike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentLikeDislike extends EntityPathBase<CommentLikeDislike> {

    private static final long serialVersionUID = -1076944760L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentLikeDislike commentLikeDislike = new QCommentLikeDislike("commentLikeDislike");

    public final org.devridge.common.dto.QBaseTimeEntity _super = new org.devridge.common.dto.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.devridge.api.domain.qna.entity.id.QCommentLikeDislikeId id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final EnumPath<org.devridge.api.domain.qna.dto.type.LikeStatus> status = createEnum("status", org.devridge.api.domain.qna.dto.type.LikeStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommentLikeDislike(String variable) {
        this(CommentLikeDislike.class, forVariable(variable), INITS);
    }

    public QCommentLikeDislike(Path<? extends CommentLikeDislike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentLikeDislike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentLikeDislike(PathMetadata metadata, PathInits inits) {
        this(CommentLikeDislike.class, metadata, inits);
    }

    public QCommentLikeDislike(Class<? extends CommentLikeDislike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new org.devridge.api.domain.qna.entity.id.QCommentLikeDislikeId(forProperty("id"), inits.get("id")) : null;
    }

}

