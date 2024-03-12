package org.devridge.api.domain.qna.interceptor;

import lombok.RequiredArgsConstructor;

import org.devridge.api.infrastructure.qna.QnACommentRepository;
import org.devridge.api.common.exception.common.DataNotFoundException;

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

import static org.devridge.api.common.util.ResponseUtil.createResponseBody;
import static org.devridge.api.common.util.SecurityContextHolderUtil.getMemberId;

@Component
@RequiredArgsConstructor
public class QnACommentAuthInterceptor implements HandlerInterceptor {

    private final QnACommentRepository qnaCommentRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestMethod = request.getMethod();

        if (requestMethod.equals("PATCH") || requestMethod.equals("DELETE")) {
            Long[] memberIdAndWriterId = this.getPathVariables(request);

            if (!Objects.equals(memberIdAndWriterId[0], memberIdAndWriterId[1])) {
                createResponseBody(
                    response, new InterceptorErrorMessage("해당 답글에 대한 권한이 없습니다."), HttpStatus.FORBIDDEN
                );

                return false;
            }
        } else {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            String requestUri = request.getRequestURI();

            if (
                pathMatcher.match("/api/qna/*/comments/like/*", requestUri)
                || pathMatcher.match("/api/qna/*/comments/dislike/*", requestUri)
            ) {
                Long[] memberIdAndWriterId = this.getPathVariables(request);

                if (Objects.equals(memberIdAndWriterId[0], memberIdAndWriterId[1])) {
                    createResponseBody(
                        response, new InterceptorErrorMessage("내가 작성한 글은 추천/비추천을 누를 수 없습니다."), HttpStatus.FORBIDDEN
                    );

                    return false;
                }
            }
        }

        return true;
    }

    private Long[] getPathVariables(HttpServletRequest request) {
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long memberId = getMemberId();
        Long qnaCommentId = Long.parseLong((String) pathVariables.get("commentId"));
        Long writerId = qnaCommentRepository.findById(qnaCommentId)
            .orElseThrow(() -> new DataNotFoundException())
            .getMember()
            .getId();

        return new Long[] { memberId, writerId };
    }
}
