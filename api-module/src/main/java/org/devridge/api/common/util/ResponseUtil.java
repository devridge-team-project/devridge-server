package org.devridge.api.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {

    public static void createResponseBody(HttpServletResponse response, Object object, HttpStatus status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setStatus(status.value());
    }

    public static void createResponseBody(HttpServletResponse response, HttpStatus status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
    }
}
