package org.devridge.api.domain.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnA is a Querydsl query type for QnA
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnA extends EntityPathBase<QnA> {

    private static final long serialVersionUID = 327840779L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnA qnA = new QQnA("qnA");

    public final org.devridge.common.dto.QBaseEntity _super = new org.devridge.common.dto.QBaseEntity(this);

    public final ListPath<QnAComment, QQnAComment> comments = this.<QnAComment, QQnAComment>createList("comments", QnAComment.class, QQnAComment.class, PathInits.DIRECT2);

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

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QQnA(String variable) {
        this(QnA.class, forVariable(variable), INITS);
    }

    public QQnA(Path<? extends QnA> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnA(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnA(PathMetadata metadata, PathInits inits) {
        this(QnA.class, metadata, inits);
    }

    public QQnA(Class<? extends QnA> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

