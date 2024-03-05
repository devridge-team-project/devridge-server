package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoteSenderResponse {

    private String title;
    private String content;
    private String receiverName;
    private LocalDateTime sendTime;
    private LocalDateTime readAt;
}
