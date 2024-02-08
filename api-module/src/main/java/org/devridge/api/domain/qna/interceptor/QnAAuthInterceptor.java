package org.devridge.api.domain.qna.interceptor;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.repository.QnARepository;
import org.devridge.common.exception.DataNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.Map;
import java.util.Objects;

import static org.devridge.api.util.ResponseUtil.createResponseBody;
import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@Component
@RequiredArgsConstructor
public class QnAAuthInterceptor implements HandlerInterceptor {

    private final QnARepository qnaRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestMethod = request.getMethod();

        if (requestMethod.equals("PATCH") || requestMethod.equals("DELETE")) {
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long memberId = getMemberId();
            Long qnaId = Long.parseLong((String) pathVariables.get("qnaId"));
            Long writerId = qnaRepository.findById(qnaId)
                    .orElseThrow(() -> new DataNotFoundException())
                    .getMember()
                    .getId();

            if (!Objects.equals(memberId, writerId)) {
                createResponseBody(
                    response, new InterceptorErrorMessage("해당 게시글에 대한 권한이 없습니다."), HttpStatus.FORBIDDEN
                );
                return false;
            }
        } else {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            String requestUri = request.getRequestURI();

            if (pathMatcher.match("/api/qna/like/*", requestUri) || pathMatcher.match("/api/qna/dislike/*", requestUri)) {
                Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                Long memberId = getMemberId();
                Long qnaId = Long.parseLong((String) pathVariables.get("qnaId"));
                Long writerId = qnaRepository.findById(qnaId)
                        .orElseThrow(() -> new DataNotFoundException())
                        .getMember()
                        .getId();

                if (Objects.equals(memberId, writerId)) {
                    createResponseBody(
                            response, new InterceptorErrorMessage("내가 작성한 글은 추천/비추천을 누를 수 없습니다."), HttpStatus.FORBIDDEN
                    );
                    return false;
                }
            }
        }

        return true;
    }
}
