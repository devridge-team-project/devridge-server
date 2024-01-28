package org.devridge.api.domain.skill.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSkill is a Querydsl query type for Skill
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSkill extends EntityPathBase<Skill> {

    private static final long serialVersionUID = 88100235L;

    public static final QSkill skill1 = new QSkill("skill1");

    public final org.devridge.common.dto.QBaseEntity _super = new org.devridge.common.dto.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final SetPath<MemberSkill, QMemberSkill> memberSkills = this.<MemberSkill, QMemberSkill>createSet("memberSkills", MemberSkill.class, QMemberSkill.class, PathInits.DIRECT2);

    public final StringPath skill = createString("skill");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSkill(String variable) {
        super(Skill.class, forVariable(variable));
    }

    public QSkill(Path<? extends Skill> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSkill(PathMetadata metadata) {
        super(Skill.class, metadata);
    }

}

