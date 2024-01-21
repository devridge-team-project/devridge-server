package org.devridge.api.domain.community.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.ReadCommunityResponse;
import org.devridge.api.domain.community.entity.Community;
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
    public ResponseEntity<?> writingCommunity(@Valid @RequestBody CreateCommunityRequest dto) {
        communityService.createCommunity(dto);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "게시글 작성 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<?> viewCommunity(@PathVariable Long communityId) {
        Community community = communityService.getCommunityById(communityId);
        String nickName = community.getMember().getNickname();
        ReadCommunityResponse dto = ReadCommunityResponse.builder()
            .nickName(nickName)
            .title(community.getTitle())
            .content(community.getContent())
            .views(community.getViews())
            .createdAt(community.getCreatedAt())
            .updatedAt(community.getUpdatedAt())
            .build();
        // dto로 커뮤니티 글 작성자, 제목, 내용, 생성시간,수정시간 보내야함
        BaseResponse response = new BaseResponse<>(
            HttpStatus.OK.value(),
            "게시글 조회 성공",
            dto
            // dto로 커뮤니티 글 작성자, 제목, 내용, 생성시간,수정시간 보내야함
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{communityId}")
    public ResponseEntity<?> modifyCommunity(
        @PathVariable Long communityId,
        @Valid @RequestBody CreateCommunityRequest dto
    ) {
        communityService.updateCommunity(communityId, dto);
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
        List<Community> communityList = communityService.viewAllCommunity();
        List<ReadCommunityResponse> dtos = communityList.stream()
            .map(community ->
                ReadCommunityResponse.builder()
                    .nickName(community.getMember().getNickname())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .views(community.getViews())
                    .createdAt(community.getCreatedAt())
                    .updatedAt(community.getUpdatedAt())
                    .build()
            )
            .collect(Collectors.toList());
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "게시글 전체 목록 조회 성공",
            dtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

