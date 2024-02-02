package org.devridge.api.domain.community.entity.id;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityCommentLikeDislikeId is a Querydsl query type for CommunityCommentLikeDislikeId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCommunityCommentLikeDislikeId extends BeanPath<CommunityCommentLikeDislikeId> {

    private static final long serialVersionUID = 1041349886L;

    public static final QCommunityCommentLikeDislikeId communityCommentLikeDislikeId = new QCommunityCommentLikeDislikeId("communityCommentLikeDislikeId");

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QCommunityCommentLikeDislikeId(String variable) {
        super(CommunityCommentLikeDislikeId.class, forVariable(variable));
    }

    public QCommunityCommentLikeDislikeId(Path<? extends CommunityCommentLikeDislikeId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityCommentLikeDislikeId(PathMetadata metadata) {
        super(CommunityCommentLikeDislikeId.class, metadata);
    }

}

