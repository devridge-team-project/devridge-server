package org.devridge.api.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
import org.devridge.api.domain.member.service.MemberService;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;
    @PostMapping("/one")
    public ResponseEntity<?> signUp(@RequestBody CreateMemberRequest reqDto) {
        memberService.createMember(reqDto);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                "회원가입 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/one")
    public ResponseEntity<?> deleteMember(@RequestBody DeleteMemberRequest reqDto){
        memberService.deleteMember(reqDto);

        return ResponseEntity.ok().build();
    }

}
