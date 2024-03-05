package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoteResponse {

    private String title;
    private String content;
    private String senderName;
    private LocalDateTime receiveTime;
}
