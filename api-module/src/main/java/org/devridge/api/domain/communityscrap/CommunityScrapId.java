package org.devridge.api.domain.communityscrap;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class CommunityScrapId implements Serializable {
    private Long memberId;
    private Long communityId;
}
