package org.devridge.api.domain.community.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.service.CommunityService;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community")
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<?> writingCommunity(@Valid @RequestBody CreateCommunityRequest communityRequest) {
        communityService.createCommunity(communityRequest);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "게시글 작성 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<?> viewCommunity(@PathVariable Long communityId) {
        CommunityDetailResponse communityDetailResponse = communityService.getCommunityById(communityId);
        return ResponseEntity.ok().body(communityDetailResponse);
    }

    @PutMapping("/{communityId}")
    public ResponseEntity<?> modifyCommunity(
        @PathVariable Long communityId,
        @Valid @RequestBody CreateCommunityRequest communityRequest
    ) {
        communityService.updateCommunity(communityId, communityRequest);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "게시글 수정 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long communityId) {
        communityService.deleteCommunity(communityId);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "게시글 삭제 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all") // 커뮤니티 글 전체 목록 보여주기
    public ResponseEntity<?> viewAllCommunity() {
        List<CommunityDetailResponse> communityDetailResponses = communityService.viewAllCommunity();
        return ResponseEntity.ok().body(communityDetailResponses);
    }
}

