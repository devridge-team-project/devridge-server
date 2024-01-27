package org.devridge.api.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.dto.request.*;
import org.devridge.api.domain.member.dto.response.UpdateMemberResponse;
import org.devridge.api.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> signUp(@Valid @RequestBody CreateMemberRequest memberRequest) {
        Long userId = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/api/users/" + userId)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Valid @RequestBody DeleteMemberRequest memberRequest) {
        memberService.deleteMember(memberRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        memberService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.updatePassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<UpdateMemberResponse> updateMember(
            @Valid @RequestBody UpdateMemberProfileRequest updateMemberRequest
    ) {
        UpdateMemberResponse result = memberService.updateMember(updateMemberRequest);
        return ResponseEntity.ok().body(result);
    }
}
