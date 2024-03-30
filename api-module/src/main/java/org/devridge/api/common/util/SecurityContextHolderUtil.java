package org.devridge.api.common.util;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.common.security.auth.CustomMemberDetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderUtil {
    // TODO: 멀티쓰레드 환경에서 동기화 작업
    public static Long getMemberId() {
        CustomMemberDetails principal = getCustomMemberDetails();
        Long memberId = principal.getMember().getId();
        return memberId;
    }

    public static String getEmail() {
        CustomMemberDetails principal = getCustomMemberDetails();
        String email = principal.getUsername();
        return email;
    }

    public static Member getMember() {
        CustomMemberDetails principal = getCustomMemberDetails();
        return principal.getMember();
    }

    private static CustomMemberDetails getCustomMemberDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("authentication is not valid");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomMemberDetails)) {
            throw new IllegalStateException("principal is not valid");
        }

        return (CustomMemberDetails) principal;
    }
}