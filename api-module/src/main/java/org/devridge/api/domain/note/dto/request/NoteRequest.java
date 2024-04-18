package org.devridge.api.domain.note.dto.request;

import lombok.Getter;

@Getter
public class NoteRequest {

    private String content;
    private Long receiverId;
}
