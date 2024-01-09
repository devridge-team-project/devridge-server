package org.devridge.api.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TokenResponse {

    private String accessToken;
}
