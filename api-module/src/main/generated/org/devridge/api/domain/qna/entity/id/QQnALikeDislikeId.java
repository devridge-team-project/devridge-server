package org.devridge.api.domain.qna.entity.id;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnALikeDislikeId is a Querydsl query type for QnALikeDislikeId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QQnALikeDislikeId extends BeanPath<QnALikeDislikeId> {

    private static final long serialVersionUID = 879145183L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnALikeDislikeId qnALikeDislikeId = new QQnALikeDislikeId("qnALikeDislikeId");

    public final org.devridge.api.domain.member.entity.QMember member;

    public final org.devridge.api.domain.qna.entity.QQnA qna;

    public QQnALikeDislikeId(String variable) {
        this(QnALikeDislikeId.class, forVariable(variable), INITS);
    }

    public QQnALikeDislikeId(Path<? extends QnALikeDislikeId> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnALikeDislikeId(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnALikeDislikeId(PathMetadata metadata, PathInits inits) {
        this(QnALikeDislikeId.class, metadata, inits);
    }

    public QQnALikeDislikeId(Class<? extends QnALikeDislikeId> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.qna = inits.isInitialized("qna") ? new org.devridge.api.domain.qna.entity.QQnA(forProperty("qna"), inits.get("qna")) : null;
    }

}

