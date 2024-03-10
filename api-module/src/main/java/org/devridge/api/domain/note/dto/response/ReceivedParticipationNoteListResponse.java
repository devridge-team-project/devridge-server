package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;

@Getter
@Builder
public class ReceivedParticipationNoteListResponse {

    private MemberInfoResponse sendMember;
    private LocalDateTime receivedTime;
}
