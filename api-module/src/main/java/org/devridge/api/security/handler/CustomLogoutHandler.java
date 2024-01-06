package org.devridge.api.security.handler;

import org.devridge.api.util.ResponseUtil;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK.value(),
                "logout success"
        );

        ResponseUtil.createResponseMessage(response, baseResponse);

        String accessToken = request.getHeader("devAccessToken");
        response.addHeader("devAccessToken", null);
    }
}
