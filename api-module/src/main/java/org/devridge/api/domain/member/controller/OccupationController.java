package org.devridge.api.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.service.OccupationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/occupations")
public class OccupationController {

    private final OccupationService occupationService;

    @GetMapping
    public ResponseEntity<?> getAllOccupations(){
    }
}
