package org.devridge.api.domain.qna.entity.id;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentLikeDislikeId is a Querydsl query type for CommentLikeDislikeId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCommentLikeDislikeId extends BeanPath<CommentLikeDislikeId> {

    private static final long serialVersionUID = 1694203684L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentLikeDislikeId commentLikeDislikeId = new QCommentLikeDislikeId("commentLikeDislikeId");

    public final org.devridge.api.domain.member.entity.QMember member;

    public final org.devridge.api.domain.qna.entity.QQnAComment qnaComment;

    public QCommentLikeDislikeId(String variable) {
        this(CommentLikeDislikeId.class, forVariable(variable), INITS);
    }

    public QCommentLikeDislikeId(Path<? extends CommentLikeDislikeId> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentLikeDislikeId(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentLikeDislikeId(PathMetadata metadata, PathInits inits) {
        this(CommentLikeDislikeId.class, metadata, inits);
    }

    public QCommentLikeDislikeId(Class<? extends CommentLikeDislikeId> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.qnaComment = inits.isInitialized("qnaComment") ? new org.devridge.api.domain.qna.entity.QQnAComment(forProperty("qnaComment"), inits.get("qnaComment")) : null;
    }

}

