package org.devridge.api.domain.community.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityCommentLikeDislike is a Querydsl query type for CommunityCommentLikeDislike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCommentLikeDislike extends EntityPathBase<CommunityCommentLikeDislike> {

    private static final long serialVersionUID = -398420838L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityCommentLikeDislike communityCommentLikeDislike = new QCommunityCommentLikeDislike("communityCommentLikeDislike");

    public final org.devridge.common.dto.QBaseTimeEntity _super = new org.devridge.common.dto.QBaseTimeEntity(this);

    public final QCommunityComment communityComment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.devridge.api.domain.community.entity.id.QCommunityCommentLikeDislikeId id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final org.devridge.api.domain.member.entity.QMember member;

    public final EnumPath<LikeStatus> status = createEnum("status", LikeStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommunityCommentLikeDislike(String variable) {
        this(CommunityCommentLikeDislike.class, forVariable(variable), INITS);
    }

    public QCommunityCommentLikeDislike(Path<? extends CommunityCommentLikeDislike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityCommentLikeDislike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityCommentLikeDislike(PathMetadata metadata, PathInits inits) {
        this(CommunityCommentLikeDislike.class, metadata, inits);
    }

    public QCommunityCommentLikeDislike(Class<? extends CommunityCommentLikeDislike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.communityComment = inits.isInitialized("communityComment") ? new QCommunityComment(forProperty("communityComment"), inits.get("communityComment")) : null;
        this.id = inits.isInitialized("id") ? new org.devridge.api.domain.community.entity.id.QCommunityCommentLikeDislikeId(forProperty("id")) : null;
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

