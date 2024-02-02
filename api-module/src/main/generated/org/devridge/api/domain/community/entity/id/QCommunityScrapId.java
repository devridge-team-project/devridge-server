package org.devridge.api.domain.community.entity.id;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityScrapId is a Querydsl query type for CommunityScrapId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCommunityScrapId extends BeanPath<CommunityScrapId> {

    private static final long serialVersionUID = -1227265416L;

    public static final QCommunityScrapId communityScrapId = new QCommunityScrapId("communityScrapId");

    public final NumberPath<Long> communityId = createNumber("communityId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QCommunityScrapId(String variable) {
        super(CommunityScrapId.class, forVariable(variable));
    }

    public QCommunityScrapId(Path<? extends CommunityScrapId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityScrapId(PathMetadata metadata) {
        super(CommunityScrapId.class, metadata);
    }

}

