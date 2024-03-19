package org.devridge.api.presentation.controller.member;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.dto.request.*;
import org.devridge.api.domain.member.dto.response.MemberResponse;
import org.devridge.api.application.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> signUp(
            @RequestPart(value = "image") MultipartFile image,
            @RequestPart(value = "member") @Valid CreateMemberRequest memberRequest
    ) throws IOException {
        Long userId = memberService.createMember(memberRequest, image);
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
    public ResponseEntity<Void> updateMember(
            @RequestPart(value = "image") MultipartFile image,
            @RequestPart(value = "info") @Valid UpdateProfileRequest updateMemberRequest
    ) throws IOException {
        memberService.updateMember(updateMemberRequest, image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/details")
    public ResponseEntity<MemberResponse> getMemberDetails(){
        MemberResponse result = memberService.getMemberDetails();
        return ResponseEntity.ok().body(result);
    }
}
