package org.devridge.api.domain.community.entity.id;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class ProjectCommentLikeDislikeId implements Serializable {

    private Long memberId;
    private Long commentId;
}
