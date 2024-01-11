package org.devridge.api.domain.communityscrap;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class CommunityScrapId implements Serializable {

    private Long memberId;
    private Long communityId;
}
