package org.devridge.api.domain.community.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityScrap is a Querydsl query type for CommunityScrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityScrap extends EntityPathBase<CommunityScrap> {

    private static final long serialVersionUID = 1400825606L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityScrap communityScrap = new QCommunityScrap("communityScrap");

    public final org.devridge.common.dto.QBaseTimeEntity _super = new org.devridge.common.dto.QBaseTimeEntity(this);

    public final QCommunity community;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.devridge.api.domain.community.entity.id.QCommunityScrapId id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final org.devridge.api.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommunityScrap(String variable) {
        this(CommunityScrap.class, forVariable(variable), INITS);
    }

    public QCommunityScrap(Path<? extends CommunityScrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityScrap(PathMetadata metadata, PathInits inits) {
        this(CommunityScrap.class, metadata, inits);
    }

    public QCommunityScrap(Class<? extends CommunityScrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.community = inits.isInitialized("community") ? new QCommunity(forProperty("community"), inits.get("community")) : null;
        this.id = inits.isInitialized("id") ? new org.devridge.api.domain.community.entity.id.QCommunityScrapId(forProperty("id")) : null;
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

