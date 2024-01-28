package org.devridge.api.domain.qna.entity.id;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnAScrapId is a Querydsl query type for QnAScrapId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QQnAScrapId extends BeanPath<QnAScrapId> {

    private static final long serialVersionUID = -283762974L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnAScrapId qnAScrapId = new QQnAScrapId("qnAScrapId");

    public final org.devridge.api.domain.member.entity.QMember member;

    public final org.devridge.api.domain.qna.entity.QQnA qna;

    public QQnAScrapId(String variable) {
        this(QnAScrapId.class, forVariable(variable), INITS);
    }

    public QQnAScrapId(Path<? extends QnAScrapId> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnAScrapId(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnAScrapId(PathMetadata metadata, PathInits inits) {
        this(QnAScrapId.class, metadata, inits);
    }

    public QQnAScrapId(Class<? extends QnAScrapId> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.qna = inits.isInitialized("qna") ? new org.devridge.api.domain.qna.entity.QQnA(forProperty("qna"), inits.get("qna")) : null;
    }

}

