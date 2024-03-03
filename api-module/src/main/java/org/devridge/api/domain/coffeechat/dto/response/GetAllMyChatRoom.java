package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAllMyChatRoom {

    private Long id;
    private String title;
}
