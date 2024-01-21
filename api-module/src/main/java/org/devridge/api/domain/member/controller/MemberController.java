package org.devridge.api.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
import org.devridge.api.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<?> signUp(@Valid @RequestBody CreateMemberRequest memberRequest) {
        memberService.createMember(memberRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteMember(@RequestBody DeleteMemberRequest memberRequest) {
        memberService.deleteMember(memberRequest);
        return ResponseEntity.ok().build();
    }
}
