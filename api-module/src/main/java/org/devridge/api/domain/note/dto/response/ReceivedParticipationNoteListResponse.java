package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.dto.UserInformation;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedParticipationNoteListResponse {

    private Long participationId;
    private UserInformation sendMember;
    private LocalDateTime receivedTime;
    private Boolean isApproved;
    private String content;
}
