package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAllNote {

    private Long id;
    private String content;
    private Long senderId;
    private LocalDateTime createdAt;
}
