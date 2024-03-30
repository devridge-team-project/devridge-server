package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.devridge.api.common.dto.UserInformation;

@Getter
@Builder
public class SentParticipationNoteDetailResponse {

    private UserInformation receiveMember;
    private String content;
    private LocalDateTime sendTime;
    private Boolean isApproved;
    private String category;
    private Long categoryId;
}
