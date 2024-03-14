package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.devridge.api.common.dto.UserInformation;

@Getter
@AllArgsConstructor
public class GetAllMyChatRoom {

    private Long id;
    private UserInformation member;
}
