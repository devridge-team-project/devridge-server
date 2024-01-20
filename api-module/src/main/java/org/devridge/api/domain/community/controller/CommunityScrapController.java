package org.devridge.api.domain.community.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.service.CommunityScrapService;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/scrap")
@RestController
public class CommunityScrapController {

    private final CommunityScrapService communityScrapService;

    @PostMapping("/{communityId}")
    public ResponseEntity<?> scrapCreate(@PathVariable Long communityId) {
        communityScrapService.createScrap(communityId);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "스크랩 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity<?> scrapdelete(@PathVariable Long communityId) {
        communityScrapService.deleteScrap(communityId);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "스크랩 삭제 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
