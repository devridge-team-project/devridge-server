package org.devridge.api.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
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

    @PostMapping("")
    public ResponseEntity<Void> signUp(@Valid @RequestBody CreateMemberRequest memberRequest) {
        Long userId = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/api/users/" + userId)).build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteMemberRequest memberRequest) {
        memberService.deleteMember(memberRequest);
        return ResponseEntity.ok().build();
    }
}
