package org.devridge.api.domain.skill.entity.key;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberSkillId is a Querydsl query type for MemberSkillId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMemberSkillId extends BeanPath<MemberSkillId> {

    private static final long serialVersionUID = -183729603L;

    public static final QMemberSkillId memberSkillId = new QMemberSkillId("memberSkillId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> skillId = createNumber("skillId", Long.class);

    public QMemberSkillId(String variable) {
        super(MemberSkillId.class, forVariable(variable));
    }

    public QMemberSkillId(Path<? extends MemberSkillId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberSkillId(PathMetadata metadata) {
        super(MemberSkillId.class, metadata);
    }

}

