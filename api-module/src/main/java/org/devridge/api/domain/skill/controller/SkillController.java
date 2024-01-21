package org.devridge.api.domain.skill.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.skill.dto.response.SkillInformation;
import org.devridge.api.domain.skill.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skills")
public class SkillController {
    
    private final SkillService skillService;

    @GetMapping("")
    public ResponseEntity<?> getAllSkills(){
        List<SkillInformation> result = skillService.getAllSkills();
        return ResponseEntity.ok().body(result);
    }
}
