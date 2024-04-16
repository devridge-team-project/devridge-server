package org.devridge.api.domain.note.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.devridge.api.common.dto.UserInformation;

@Getter
@Builder
public class GetAllRoom {

    private Long id;
    private UserInformation userInformation;
    private String content;
    private LocalDateTime createAt;
}
