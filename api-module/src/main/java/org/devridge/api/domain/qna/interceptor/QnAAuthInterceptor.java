package org.devridge.api.domain.qna.interceptor;

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

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@Component
@RequiredArgsConstructor
public class QnAAuthInterceptor implements HandlerInterceptor {

    private final QnARepository qnaRepository;

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

            if (memberId != writerId) {
                response.getOutputStream().println("NO");
                return false;
            }
        }

        return true;
    }
}
