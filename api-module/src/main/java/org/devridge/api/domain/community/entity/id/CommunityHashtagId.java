package org.devridge.api.domain.community.entity.id;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommunityHashtagId implements Serializable {

    private Long communityId;
    private Long hashtagId;
}
