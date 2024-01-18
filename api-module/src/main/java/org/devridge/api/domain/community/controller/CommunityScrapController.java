package org.devridge.api.domain.community.controller;

import org.devridge.api.domain.community.service.CommunityScrapService;
import org.devridge.common.dto.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/community/scrap")
@RestController
public class CommunityScrapController {

    private CommunityScrapService communityScrapService;

    @Autowired
    public CommunityScrapController(CommunityScrapService communityScrapService) {
        this.communityScrapService = communityScrapService;
    }

    @PostMapping("/{communityId}")
    public ResponseEntity<?> scrapCreate(@PathVariable Long memberId,
        @PathVariable Long communityId) {
        communityScrapService.createScrap(memberId, communityId);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "스크랩 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity<?> scrapdelete(@PathVariable Long memberId,
        @PathVariable Long communityId) {
        communityScrapService.deleteScrap(memberId, communityId);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "스크랩 삭제 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
