package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceivedNoteDetailResponse {

    private String senderName;
    private String title;
    private String content;
    private LocalDateTime receivedTime;
}
