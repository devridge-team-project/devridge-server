package org.devridge.api.presentation.controller.member;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.dto.response.OccupationInformation;
import org.devridge.api.application.member.OccupationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/occupations")
public class OccupationController {

    private final OccupationService occupationService;

    @GetMapping
    public ResponseEntity<List<OccupationInformation>> getAllOccupations(){
        List<OccupationInformation> result = occupationService.getAllOccupations();
        return ResponseEntity.ok().body(result);
    }
}
