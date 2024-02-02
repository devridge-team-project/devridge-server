package org.devridge.api.domain.skill.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberSkill is a Querydsl query type for MemberSkill
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberSkill extends EntityPathBase<MemberSkill> {

    private static final long serialVersionUID = 1850118033L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberSkill memberSkill = new QMemberSkill("memberSkill");

    public final org.devridge.common.dto.QBaseTimeEntity _super = new org.devridge.common.dto.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.devridge.api.domain.skill.entity.key.QMemberSkillId id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final org.devridge.api.domain.member.entity.QMember member;

    public final QSkill skill;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberSkill(String variable) {
        this(MemberSkill.class, forVariable(variable), INITS);
    }

    public QMemberSkill(Path<? extends MemberSkill> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberSkill(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberSkill(PathMetadata metadata, PathInits inits) {
        this(MemberSkill.class, metadata, inits);
    }

    public QMemberSkill(Class<? extends MemberSkill> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new org.devridge.api.domain.skill.entity.key.QMemberSkillId(forProperty("id")) : null;
        this.member = inits.isInitialized("member") ? new org.devridge.api.domain.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.skill = inits.isInitialized("skill") ? new QSkill(forProperty("skill")) : null;
    }

}

