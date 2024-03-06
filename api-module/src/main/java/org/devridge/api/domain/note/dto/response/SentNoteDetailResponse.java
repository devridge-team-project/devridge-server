package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SentNoteDetailResponse {

    private String receiverName;
    private LocalDateTime sentTime;
    private String title;
    private String content;
}
