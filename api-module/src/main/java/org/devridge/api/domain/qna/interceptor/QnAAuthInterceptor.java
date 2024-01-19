package org.devridge.api.domain.qna.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.repository.QnARepository;
import org.devridge.common.exception.DataNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.Map;
import java.util.Objects;

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@Component
@RequiredArgsConstructor
public class QnAAuthInterceptor implements HandlerInterceptor {

    private final QnARepository qnaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestMethod = request.getMethod();

        if (requestMethod.equals("PUT") || requestMethod.equals("DELETE")) {
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long memberId = getMemberId();
            Long qnaId = Long.parseLong((String) pathVariables.get("qnaId"));
            Long writerId = qnaRepository.findById(qnaId)
                    .orElseThrow(() -> new DataNotFoundException())
                    .getMember()
                    .getId();

            if (!Objects.equals(memberId, writerId)) {
                String result = objectMapper.writeValueAsString(new InterceptorErrorMessage("해당 게시글에 대한 권한이 없습니다."));

                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.setStatus(403);
                response.getWriter().write(result);

                return false;
            }
        }

        return true;
    }
}
