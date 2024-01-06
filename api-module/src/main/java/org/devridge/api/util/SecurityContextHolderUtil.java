package org.devridge.api.util;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.security.auth.CustomMemberDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHolderUtil {

    public static Long getMemberId(){
        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = principal.getMember().getId();
        return memberId;
    }

    public static String getEmail(){
        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getUsername();
        return email;
    }

    public static Member getMember(){
        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getMember();
    }
}
