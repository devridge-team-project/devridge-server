package org.devridge.api.domain.emailverification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailVerification is a Querydsl query type for EmailVerification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailVerification extends EntityPathBase<EmailVerification> {

    private static final long serialVersionUID = 854236523L;

    public static final QEmailVerification emailVerification = new QEmailVerification("emailVerification");

    public final org.devridge.common.dto.QBaseEntity _super = new org.devridge.common.dto.QBaseEntity(this);

    public final BooleanPath checkStatus = createBoolean("checkStatus");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> expireAt = createDateTime("expireAt", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final StringPath receiptEmail = createString("receiptEmail");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEmailVerification(String variable) {
        super(EmailVerification.class, forVariable(variable));
    }

    public QEmailVerification(Path<? extends EmailVerification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailVerification(PathMetadata metadata) {
        super(EmailVerification.class, metadata);
    }

}

