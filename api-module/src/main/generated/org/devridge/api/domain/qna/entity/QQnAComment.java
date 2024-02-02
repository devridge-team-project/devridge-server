package org.devridge.api.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnAComment is a Querydsl query type for QnAComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnAComment extends EntityPathBase<QnAComment> {

    private static final long serialVersionUID = -669614124L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnAComment qnAComment = new QQnAComment("qnAComment");

    public final org.devridge.common.dto.QBaseEntity _super = new org.devridge.common.dto.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> dislikes = createNumber("dislikes", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final org.devridge.api.domain.member.entity.QMember member;

    public final QQnA qna;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQnAComment(String variable) {
        this(QnAComment.class, forVariable(variable), INITS);
    }

    public QQnAComment(Path<? extends QnAComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnAComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnAComment(PathMetadata metadata, PathInits inits) {
        this(QnAComment.class, metadata, inits);
    }

    public QQnAComment(Class<? extends QnAComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.qna = inits.isInitialized("qna") ? new QQnA(forProperty("qna"), inits.get("qna")) : null;
    }

}

