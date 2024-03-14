package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.devridge.api.common.dto.UserInformation;

@Getter
@Builder
public class ReceivedParticipationNoteDetailResponse {

    private UserInformation sendMember;
    private String content;
    private LocalDateTime receiveTime;
}
