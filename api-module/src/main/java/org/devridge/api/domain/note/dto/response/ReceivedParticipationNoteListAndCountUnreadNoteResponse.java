package org.devridge.api.domain.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
public class ReceivedParticipationNoteListAndCountUnreadNoteResponse {

    private int countUnreadNote;
    private Slice<ReceivedParticipationNoteListResponse> receivedParticipationNoteListResponses;
}
